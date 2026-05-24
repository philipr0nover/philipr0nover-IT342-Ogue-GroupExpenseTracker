package com.ogue.groupexpensetracker.mobile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(onLoginSuccess: (UserResponse) -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                val api = ApiClient.retrofit.create(ApiService::class.java)
                val login = LoginRequest(email, password)

                api.login(login).enqueue(object : Callback<UserResponse> {

                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    ) {

                        if (response.isSuccessful) {

                            val user = response.body()

                            if (user != null) {

                                Toast.makeText(
                                    context,
                                    "Welcome ${user.firstname} ${user.lastname}",
                                    Toast.LENGTH_LONG
                                ).show()

                                // 🔥 THIS TRIGGERS DASHBOARD
                                onLoginSuccess(user)

                            } else {
                                Toast.makeText(
                                    context,
                                    "Login failed (empty response)",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "Invalid credentials",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}