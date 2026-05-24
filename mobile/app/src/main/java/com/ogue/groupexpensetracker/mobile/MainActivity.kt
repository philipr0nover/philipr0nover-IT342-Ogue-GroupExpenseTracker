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
            var selectedGroupId by remember { mutableStateOf<Long?>(null) }

            when {
                user == null -> {

                    // 🔐 LOGIN
                    LoginScreen(
                        onLoginSuccess = { loggedInUser ->
                            user = loggedInUser
                        }
                    )
                }

                selectedGroupId != null -> {

                    // 💸 ADD EXPENSE
                    AddExpenseScreen(
                        groupId = selectedGroupId!!,
                        userId = user!!.id,
                        onBack = {
                            selectedGroupId = null
                        }
                    )
                }

                else -> {

                    // 🏠 DASHBOARD
                    DashboardScreen(
                        userId = user!!.id,
                        firstname = user!!.firstname,
                        lastname = user!!.lastname,

                        // 🔥 IMPORTANT (you were missing behavior control)
                        onAddExpense = { groupId ->
                            selectedGroupId = groupId
                        }
                    )
                }
            }
        }
    }
}