package com.ogue.groupexpensetracker.mobile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
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

// ─── Brand Colors (matching web dashboard) ───────────────────────────────────
private val Green       = Color(0xFF10B981)
private val GreenDark   = Color(0xFF059669)
private val GreenLight  = Color(0xFFD1FAE5)
private val GreenText   = Color(0xFF065F46)
private val Background  = Color(0xFFF9FAFB)
private val CardBg      = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF111827)
private val TextSecond  = Color(0xFF6B7280)
private val Divider     = Color(0xFFE5E7EB)
private val AmountGreen = Color(0xFF10B981)

@Composable
fun DashboardScreen(
    userId: Long,
    firstname: String,
    lastname: String,
    onAddExpense: (Long) -> Unit
) {
    var groups   by remember { mutableStateOf<List<Group>>(emptyList()) }
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val api     = remember { ApiClient.retrofit.create(ApiService::class.java) }

    LaunchedEffect(userId) {
        api.getGroups(userId).enqueue(object : Callback<List<Group>> {
            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                if (response.isSuccessful) groups = response.body() ?: emptyList()
            }
            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Toast.makeText(context, "Groups error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        api.getUserExpenses(userId).enqueue(object : Callback<List<Expense>> {
            override fun onResponse(call: Call<List<Expense>>, response: Response<List<Expense>>) {
                isLoading = false
                if (response.isSuccessful) expenses = response.body() ?: emptyList()
            }
            override fun onFailure(call: Call<List<Expense>>, t: Throwable) {
                isLoading = false
                Toast.makeText(context, "Expenses error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    val totalExpenses = expenses.sumOf { it.amount }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {

            // ── TOP BAR ──────────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Green)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ExpenseTracker",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(GreenDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = firstname.take(1).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // ── PAGE TITLE ───────────────────────────────────────────────────
            item {
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                )
            }

            // ── STATS CARDS ──────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Expenses card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Total Expenses",
                                color = TextSecond,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "₱${"%.2f".format(totalExpenses)}",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }
                    }

                    // Groups card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Groups",
                                color = TextSecond,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "${groups.size}",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── RECENT EXPENSES TABLE ────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        // Card header
                        Text(
                            text = "Recent Expenses",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = TextPrimary,
                            modifier = Modifier.padding(16.dp)
                        )

                        HorizontalDivider(color = Divider)

                        // Table header row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Green)
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Title",  color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(2f))
                            Text("Amount", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        }

                        // Table rows
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Green, strokeWidth = 2.dp)
                            }
                        } else if (expenses.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No expenses yet", color = TextSecond, fontSize = 14.sp)
                            }
                        } else {
                            expenses.take(5).forEachIndexed { index, expense ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (index % 2 == 0) Color.White else Color(0xFFF9FAFB)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        expense.description,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(2f)
                                    )
                                    Text(
                                        "₱${"%.2f".format(expense.amount)}",
                                        color = AmountGreen,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (index < minOf(expenses.size, 5) - 1) {
                                    HorizontalDivider(color = Divider, thickness = 0.5.dp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── YOUR GROUPS ──────────────────────────────────────────────────
            item {
                Text(
                    text = "Your Groups",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator(color = Green) }
                }
            } else if (groups.isEmpty()) {
                item {
                    Text(
                        "No groups found",
                        color = TextSecond,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            } else {
                items(groups) { group ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp)
                            .clickable { onAddExpense(group.id) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Green circle avatar
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(GreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = group.name.take(1).uppercase(),
                                    color = GreenText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    group.name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    "${group.members} members",
                                    color = TextSecond,
                                    fontSize = 13.sp
                                )
                            }

                            // Arrow indicator
                            Text("›", color = TextSecond, fontSize = 22.sp)
                        }
                    }
                }
            }
        }

        // ── FAB ─────────────────────────────────────────────────────────────
        FloatingActionButton(
            onClick = {
                if (groups.isNotEmpty()) {
                    onAddExpense(groups.first().id)
                } else {
                    Toast.makeText(context, "No groups available", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = Green,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Expense")
        }
    }
}