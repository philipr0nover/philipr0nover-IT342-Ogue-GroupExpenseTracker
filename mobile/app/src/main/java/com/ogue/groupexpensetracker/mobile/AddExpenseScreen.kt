package com.ogue.groupexpensetracker.mobile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ─── Brand Colors (Updated to match the login concept theme) ────────────────
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
fun AddExpenseScreen(
    groupId: Long,
    userId: Long,
    onBack: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var amount      by remember { mutableStateOf("") }
    var isLoading   by remember { mutableStateOf(false) }
    var descError   by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }

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
                        text = "Add Expense",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            // ── FORM CARD ─────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Subtitle
                Text(
                    text = "Fill in the details below to record a new expense.",
                    color = TextSecond,
                    fontSize = 14.sp
                )

                // ── Description Field ────────────────────────────────────────
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Description",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = {
                                description = it
                                descError = false
                            },
                            placeholder = { Text("e.g. Dinner, Groceries...", color = TextSecond) },
                            isError = descError,
                            supportingText = if (descError) {
                                { Text("Description is required", color = ErrorRed, fontSize = 12.sp) }
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

                // ── Amount Field ─────────────────────────────────────────────
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Amount (₱)",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = {
                                amount = it
                                amountError = false
                            },
                            placeholder = { Text("0.00", color = TextSecond) },
                            isError = amountError,
                            supportingText = if (amountError) {
                                { Text("Enter a valid amount", color = ErrorRed, fontSize = 12.sp) }
                            } else null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Green,
                                unfocusedBorderColor = Divider,
                                focusedLabelColor = Green,
                                cursorColor = Green
                            ),
                            singleLine = true,
                            prefix = { Text("₱ ", color = Green, fontWeight = FontWeight.SemiBold) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ── Submit Button ────────────────────────────────────────────
                Button(
                    onClick = {
                        // Validate
                        descError   = description.isBlank()
                        amountError = amount.toDoubleOrNull() == null || amount.toDoubleOrNull()!! <= 0

                        if (descError || amountError) return@Button

                        isLoading = true

                        val expense = Expense(
                            description = description.trim(),
                            amount      = amount.toDouble(),
                            groupId     = groupId,
                            paidBy      = userId
                        )

                        api.addExpense(expense).enqueue(object : Callback<Expense> {
                            override fun onResponse(
                                call: Call<Expense>,
                                response: Response<Expense>
                            ) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Expense added!", Toast.LENGTH_SHORT).show()
                                    onBack()
                                } else {
                                    Toast.makeText(context, "Failed to add expense", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Expense>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
                            "Add Expense",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }

                // ── Cancel Button ────────────────────────────────────────────
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
                        "Cancel",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Green
                    )
                }
            }
        }
    }
}