package com.ogue.groupexpensetracker.mobile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ─── Brand Colors (Consistent across all screens) ───────────────────────────
private val Green       = Color(0xFF26C69A) // Main concept green theme
private val GreenDark   = Color(0xFF1BAB84) // Balanced darker tone for header contrast
private val GreenLight  = Color(0xFFE3F8F2) // Clean mint tint background
private val Background  = Color(0xFFF1F4F9) // Concept off-white app background
private val CardBg      = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF111827)
private val TextSecond  = Color(0xFF6B7280)
private val Divider     = Color(0xFFE5E7EB)
private val ErrorRed    = Color(0xFFEF4444)

@Composable
fun CreateGroupScreen(
    userId: Long,
    onBack: () -> Unit
) {
    var groupName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val api     = remember { ApiClient.retrofit.create(ApiService::class.java) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── TOP BAR ──────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Green)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GreenDark)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Create Group",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            // ── FORM CONTENT ─────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Subtitle description matching the app layout structure
                Text(
                    text = "Start a new group to track split expenses, dinner tabs, and shared balances together.",
                    color = TextSecond,
                    fontSize = 14.sp
                )

                // ── Group Name Input Card ────────────────────────────────────
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Group Name",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = groupName,
                            onValueChange = {
                                groupName = it
                                nameError = false
                            },
                            placeholder = { Text("e.g. Roommates, Trip to Japan...", color = TextSecond) },
                            isError = nameError,
                            supportingText = if (nameError) {
                                { Text("Group name cannot be blank", color = ErrorRed, fontSize = 12.sp) }
                            } else null,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Green,
                                unfocusedBorderColor = Divider,
                                focusedLabelColor = Green,
                                cursorColor = Green
                            ),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── Submit Group Button ──────────────────────────────────────
                Button(
                    onClick = {
                        // Input Validation check
                        nameError = groupName.isBlank()
                        if (nameError) return@Button

                        isLoading = true

                        val request = CreateGroupRequest(
                            name = groupName.trim(),
                            createdBy = userId
                        )

                        // 🔄 Step 1: Hit the groups creation endpoint
                        api.createGroup(request).enqueue(object : Callback<Group> {
                            override fun onResponse(call: Call<Group>, response: Response<Group>) {
                                if (response.isSuccessful) {
                                    val createdGroup = response.body()

                                    if (createdGroup != null) {
                                        // 🔄 Step 2: Push relationship to bridge table so it hits the dashboard filter
                                        val memberRequest = AddMemberRequest(
                                            groupId = createdGroup.id,
                                            userId = userId
                                        )

                                        api.addGroupMember(memberRequest).enqueue(object : Callback<AddMemberRequest> {
                                            override fun onResponse(
                                                call: Call<AddMemberRequest>,
                                                memberResponse: Response<AddMemberRequest>
                                            ) {
                                                isLoading = false
                                                Toast.makeText(context, "Group created successfully!", Toast.LENGTH_SHORT).show()
                                                onBack() // Seamless navigation return to main dashboard grid
                                            }

                                            override fun onFailure(call: Call<AddMemberRequest>, t: Throwable) {
                                                isLoading = false
                                                // Fallback graceful exit if group succeeded but membership linked slow
                                                onBack()
                                            }
                                        })
                                    } else {
                                        isLoading = false
                                        onBack()
                                    }
                                } else {
                                    isLoading = false
                                    Toast.makeText(context, "Failed to create group", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Group>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green,
                        disabledContainerColor = GreenLight
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Create Group",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }

                // ── Cancel/Abortion Button ────────────────────────────────────
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Green),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Green
                    )
                }
            }
        }
    }
}