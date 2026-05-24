package com.ogue.groupexpensetracker.mobile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun AddExpenseScreen(
    groupId: Long,
    userId: Long,
    onBack: () -> Unit
) {

    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("Add Expense", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                val api = ApiClient.retrofit.create(ApiService::class.java)

                val expense = Expense(
                    description = description,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    groupId = groupId,
                    paidBy = userId
                )

                api.addExpense(expense).enqueue(object : Callback<Expense> {

                    override fun onResponse(
                        call: Call<Expense>,
                        response: Response<Expense>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Expense added", Toast.LENGTH_SHORT).show()
                            onBack()
                        } else {
                            Toast.makeText(context, "Failed to add expense", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Expense>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Expense")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onBack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}