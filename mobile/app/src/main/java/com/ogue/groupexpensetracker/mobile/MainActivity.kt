package com.ogue.groupexpensetracker.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.ogue.groupexpensetracker.mobile.ui.theme.GroupExpenseMobileTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GroupExpenseMobileTheme {
                Surface {
                    var user            by remember { mutableStateOf<UserResponse?>(null) }
                    var selectedGroupId by remember { mutableStateOf<Long?>(null) }
                    var viewingGroupId  by remember { mutableStateOf<Long?>(null) }
                    var isCreatingGroup by remember { mutableStateOf(false) }

                    when {

                        // 🔐 LOGIN
                        user == null -> {
                            LoginScreen(
                                onLoginSuccess = { loggedInUser ->
                                    user = loggedInUser
                                }
                            )
                        }

                        // 💸 ADD EXPENSE standalone screen
                        selectedGroupId != null -> {
                            AddExpenseScreen(
                                groupId = selectedGroupId!!,
                                userId  = user!!.id,
                                onBack  = { selectedGroupId = null }
                            )
                        }

                        // 🔍 GROUP DETAIL
                        viewingGroupId != null -> {
                            GroupDetailScreen(
                                groupId = viewingGroupId!!,
                                userId  = user!!.id,
                                onBack  = { viewingGroupId = null }
                            )
                        }

                        // ➕ CREATE GROUP
                        isCreatingGroup -> {
                            CreateGroupScreen(
                                userId = user!!.id,
                                onBack = { isCreatingGroup = false }
                            )
                        }

                        // 🏠 DASHBOARD
                        else -> {
                            DashboardScreen(
                                userId        = user!!.id,
                                firstname     = user!!.firstname,
                                lastname      = user!!.lastname,
                                onAddExpense  = { groupId ->
                                    selectedGroupId = groupId
                                },
                                onCreateGroup = {
                                    isCreatingGroup = true
                                },
                                onGroupClick  = { groupId ->
                                    viewingGroupId = groupId
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}