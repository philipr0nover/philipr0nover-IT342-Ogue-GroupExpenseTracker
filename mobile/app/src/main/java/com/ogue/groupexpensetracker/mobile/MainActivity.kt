package com.ogue.groupexpensetracker.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*

// ─── Screen Destinations Enums ──────────────────────────────────────────────
enum class Screen {
    LOGIN,
    DASHBOARD,
    ADD_EXPENSE,
    CREATE_GROUP
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var currentScreen by remember { mutableStateOf(Screen.LOGIN) }
            var user by remember { mutableStateOf<UserResponse?>(null) }
            var selectedGroupId by remember { mutableStateOf<Long?>(null) }

            when (currentScreen) {
                Screen.LOGIN -> {
                    // 🔐 LOGIN SCREEN
                    LoginScreen(
                        onLoginSuccess = { loggedInUser ->
                            user = loggedInUser
                            currentScreen = Screen.DASHBOARD
                        }
                    )
                }

                Screen.DASHBOARD -> {
                    // 🏠 DASHBOARD SCREEN
                    DashboardScreen(
                        userId = user!!.id,
                        firstname = user!!.firstname,
                        lastname = user!!.lastname,
                        onAddExpense = { groupId ->
                            selectedGroupId = groupId
                            currentScreen = Screen.ADD_EXPENSE
                        },
                        onCreateGroup = {
                            currentScreen = Screen.CREATE_GROUP
                        }
                    )
                }

                Screen.ADD_EXPENSE -> {
                    // 💸 ADD EXPENSE SCREEN
                    AddExpenseScreen(
                        groupId = selectedGroupId!!,
                        userId = user!!.id,
                        onBack = {
                            selectedGroupId = null
                            currentScreen = Screen.DASHBOARD
                        }
                    )
                }

                Screen.CREATE_GROUP -> {
                    // 👥 CREATE GROUP SCREEN (🔥 NEW NAVIGATION STATE ROUTE)
                    CreateGroupScreen(
                        userId = user!!.id,
                        onBack = {
                            currentScreen = Screen.DASHBOARD
                        }
                    )
                }
            }
        }
    }
}