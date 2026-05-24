package com.ogue.groupexpensetracker.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var user by remember { mutableStateOf<UserResponse?>(null) }

            if (user == null) {

                LoginScreen(
                    onLoginSuccess = { loggedInUser ->
                        user = loggedInUser
                    }
                )

            } else {

                DashboardScreen(
                    userId = user!!.id,   // ✅ FIX
                    firstname = user!!.firstname,
                    lastname = user!!.lastname
                )
            }
        }
    }
}