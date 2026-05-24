package com.ogue.groupexpensetracker.mobile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val ThemeGreen = Color(0xFF26C69A)
private val OffWhite   = Color(0xFFF1F4F9)
private val InputBg    = Color(0xFFE8EFEF)

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        // ── TOP SECTION: Green Branding Banner ────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(ThemeGreen)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sign up to easily track, split, and calculate bills and shared expenses with anyone.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )
        }

        // ── BOTTOM SECTION: Form Inputs ──────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.2f)
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()), // Handles shorter screens when keyboard is open
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            // First Name Field
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = { Text("First Name", color = Color.Gray) },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = InputBg, unfocusedContainerColor = InputBg,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = ThemeGreen
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Last Name Field
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = { Text("Last Name", color = Color.Gray) },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = InputBg, unfocusedContainerColor = InputBg,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = ThemeGreen
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email Field
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = Color.Gray) },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = InputBg, unfocusedContainerColor = InputBg,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = ThemeGreen
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password Field
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = InputBg, unfocusedContainerColor = InputBg,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = ThemeGreen
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register Action Button
            Button(
                onClick = {
                    if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val api = ApiClient.retrofit.create(ApiService::class.java)
                    val registerRequest = RegisterRequest(firstName, lastName, email, password)

                    api.register(registerRequest).enqueue(object : Callback<UserResponse> {
                        override fun onResponse(
                            call: Call<UserResponse>,
                            response: Response<UserResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Account created successfully!", Toast.LENGTH_LONG).show()
                                onRegisterSuccess()
                            } else {
                                Toast.makeText(context, "Registration failed or email already exists", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            Toast.makeText(context, "Connection Error: ${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
                },
                colors = ButtonDefaults.buttonColors(containerColor = ThemeGreen),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "REGISTER",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onNavigateToLogin
            ) {
                Text(
                    text = "Already have an account? Sign In",
                    color = Color(0xFF6366F1),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}