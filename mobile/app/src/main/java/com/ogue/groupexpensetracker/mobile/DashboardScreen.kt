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
    lastname: String
) {

    var groups by remember { mutableStateOf<List<Group>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {

        val api = ApiClient.retrofit.create(ApiService::class.java)

        api.getGroups(userId).enqueue(object : Callback<List<Group>> {

            override fun onResponse(
                call: Call<List<Group>>,
                response: Response<List<Group>>
            ) {
                isLoading = false

                if (response.isSuccessful) {
                    groups = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "Failed to load groups", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                isLoading = false
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Welcome $firstname $lastname",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

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
                                Toast.makeText(
                                    context,
                                    "Clicked ${group.name}",
                                    Toast.LENGTH_SHORT
                                ).show()
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