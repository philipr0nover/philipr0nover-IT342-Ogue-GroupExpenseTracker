package com.ogue.groupexpensetracker.mobile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DashboardScreen(
    userId: Long,
    firstname: String,
    lastname: String,
    onAddExpense: (Long) -> Unit
) {

    var groups by remember { mutableStateOf<List<Group>>(emptyList()) }
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }

    var isLoading by remember { mutableStateOf(true) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val api = ApiClient.retrofit.create(ApiService::class.java)

    // 🔥 LOAD DATA
    LaunchedEffect(Unit) {

        api.getGroups(userId).enqueue(object : Callback<List<Group>> {
            override fun onResponse(
                call: Call<List<Group>>,
                response: Response<List<Group>>
            ) {
                if (response.isSuccessful) {
                    groups = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Toast.makeText(context, "Groups error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        api.getUserExpenses(userId).enqueue(object : Callback<List<Expense>> {
            override fun onResponse(
                call: Call<List<Expense>>,
                response: Response<List<Expense>>
            ) {
                isLoading = false

                if (response.isSuccessful) {
                    expenses = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<Expense>>, t: Throwable) {
                isLoading = false
                Toast.makeText(context, "Expenses error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    val totalExpenses = expenses.sumOf { it.amount }
    val groupCount = groups.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // 🔥 HEADER
        Text(
            text = "Welcome $firstname $lastname",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔥 ADD EXPENSE BUTTON (VISIBLE NOW)
        Button(
            onClick = {
                if (groups.isNotEmpty()) {
                    onAddExpense(groups.first().id)
                } else {
                    Toast.makeText(context, "No groups available", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Expense")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔥 STATS
        Row(modifier = Modifier.fillMaxWidth()) {

            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Expenses")
                    Text("₱${"%.2f".format(totalExpenses)}")
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Groups")
                    Text("$groupCount")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔥 RECENT EXPENSES
        Text("Recent Expenses", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(10.dp))

        if (expenses.isEmpty()) {
            Text("No expenses yet")
        } else {
            LazyColumn {
                items(expenses.take(5)) { expense ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(expense.description)
                        Text("₱${"%.2f".format(expense.amount)}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔥 GROUPS
        Text("Your Groups", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(10.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else if (groups.isEmpty()) {
            Text("No groups found")
        } else {

            LazyColumn {

                items(groups) { group ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                onAddExpense(group.id) // 🔥 CLICK GROUP
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(group.name)
                            Text("Members: ${group.members}")
                        }
                    }
                }
            }
        }
    }
}