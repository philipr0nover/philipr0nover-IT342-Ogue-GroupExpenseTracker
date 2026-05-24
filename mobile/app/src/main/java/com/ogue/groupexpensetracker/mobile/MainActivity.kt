package com.ogue.groupexpensetracker.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.ogue.groupexpensetracker.mobile.ui.theme.GroupExpenseMobileTheme

// Navigation States Enum
enum class Screen {
    LOGIN,
    REGISTER,
    DASHBOARD,
    GROUP_DETAILS,
    ADD_EXPENSE,
    CREATE_GROUP
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GroupExpenseMobileTheme {
                Surface {
                    // Global session object
                    var user by remember { mutableStateOf<UserResponse?>(null) }

                    // Unified navigation state management
                    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }

                    // Route parameters tracking
                    var selectedGroupId by remember { mutableStateOf<Long?>(null) }
                    var viewingGroupId  by remember { mutableStateOf<Long?>(null) }

                    // Safety redirect: If user logs out or session drops, kick back to login routing
                    if (user == null && currentScreen != Screen.REGISTER) {
                        currentScreen = Screen.LOGIN
                    }

                    when (currentScreen) {

                        // 🔐 LOGIN VIEW
                        Screen.LOGIN -> {
                            LoginScreen(
                                onLoginSuccess = { loggedInUser ->
                                    user = loggedInUser
                                    currentScreen = Screen.DASHBOARD
                                },
                                onNavigateToRegister = {
                                    currentScreen = Screen.REGISTER
                                }
                            )
                        }

                        // 📝 REGISTER VIEW
                        Screen.REGISTER -> {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    // Send user back to sign in with their brand new credentials
                                    currentScreen = Screen.LOGIN
                                },
                                onNavigateToLogin = {
                                    currentScreen = Screen.LOGIN
                                }
                            )
                        }

                        // 💸 ADD EXPENSE SCREEN
                        Screen.ADD_EXPENSE -> {
                            AddExpenseScreen(
                                groupId = selectedGroupId ?: 0L,
                                userId  = user?.id ?: 0L,
                                onBack  = {
                                    selectedGroupId = null
                                    currentScreen = Screen.DASHBOARD
                                }
                            )
                        }

                        // 🔍 GROUP DETAIL SCREEN
                        Screen.GROUP_DETAILS -> {
                            GroupDetailScreen(
                                groupId = viewingGroupId ?: 0L,
                                userId  = user?.id ?: 0L,
                                onBack  = {
                                    viewingGroupId = null
                                    currentScreen = Screen.DASHBOARD
                                }
                            )
                        }

                        // ➕ CREATE GROUP SCREEN
                        Screen.CREATE_GROUP -> {
                            CreateGroupScreen(
                                userId = user?.id ?: 0L,
                                onBack = { currentScreen = Screen.DASHBOARD }
                            )
                        }

                        // 🏠 MAIN DASHBOARD VIEW
                        Screen.DASHBOARD -> {
                            DashboardScreen(
                                userId        = user?.id ?: 0L,
                                firstname     = user?.firstname ?: "",
                                lastname      = user?.lastname ?: "",
                                onAddExpense  = { groupId ->
                                    // 🔥 FIX: Changed check from > 0 to >= 0 to allow 0L baseline value
                                    if (groupId >= 0L) {
                                        selectedGroupId = groupId
                                        currentScreen = Screen.ADD_EXPENSE
                                    } else {
                                        currentScreen = Screen.CREATE_GROUP
                                    }
                                },
                                onCreateGroup = {
                                    currentScreen = Screen.CREATE_GROUP
                                },
                                onGroupClick  = { groupId ->
                                    viewingGroupId = groupId
                                    currentScreen = Screen.GROUP_DETAILS
                                },
                                onLogout      = {
                                    // Complete session clearing state reset
                                    user            = null
                                    selectedGroupId = null
                                    viewingGroupId  = null
                                    currentScreen   = Screen.LOGIN
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}