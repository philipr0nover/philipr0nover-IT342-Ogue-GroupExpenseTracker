package com.ogue.groupexpensetracker.mobile

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// =====================
// DATA MODELS
// =====================

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserResponse(
    val id: Long,
    val firstname: String,
    val lastname: String,
    val email: String
)

data class Group(
    val id: Long,
    val name: String,
    val members: Int
)

data class GroupMemberResponse(
    val id: Long,
    val user: UserResponse
)

data class Expense(
    val id: Long,
    val title: String,
    val amount: Double
)

// =====================
// API INTERFACE
// =====================

interface ApiService {

    // LOGIN
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<UserResponse>

    // ✅ FIXED: GROUPS BY USER
    @GET("groups/user/{userId}")
    fun getGroups(@Path("userId") userId: Long): Call<List<Group>>

    // MEMBERS
    @GET("group-members/{groupId}")
    fun getMembers(@Path("groupId") groupId: Long): Call<List<GroupMemberResponse>>

    // EXPENSES
    @GET("expenses/{groupId}")
    fun getExpenses(@Path("groupId") groupId: Long): Call<List<Expense>>
}