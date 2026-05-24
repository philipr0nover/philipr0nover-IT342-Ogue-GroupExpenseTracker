package com.ogue.groupexpensetracker.mobile

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

data class CreateGroupRequest(
    val name: String,
    val createdBy: Long
)

data class AddMemberRequest(
    val groupId: Long,
    val userId: Long
)

data class GroupMemberResponse(
    val id: Long,
    val user: UserResponse
)

data class Expense(
    val id: Long? = null,
    val description: String,
    val amount: Double,
    val groupId: Long,
    val paidBy: Long
)

// =====================
// API INTERFACE
// =====================

interface ApiService {

    // 🔐 LOGIN
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<UserResponse>

    // 🔍 SEARCH USER BY EMAIL
    @GET("auth/users/search")
    fun searchUserByEmail(@Query("email") email: String): Call<UserResponse>

    // 👥 GROUPS
    @GET("groups/user/{userId}")
    fun getGroups(@Path("userId") userId: Long): Call<List<Group>>

    // ➕ CREATE GROUP
    @POST("groups")
    fun createGroup(@Body request: CreateGroupRequest): Call<Group>

    // 👥 ADD GROUP MEMBER by userId
    @POST("group-members")
    fun addGroupMember(@Body request: AddMemberRequest): Call<AddMemberRequest>

    // ❌ REMOVE MEMBER
    @DELETE("group-members/{memberId}")
    fun removeMember(
        @Path("memberId") memberId: Long,
        @Query("requesterId") requesterId: Long
    ): Call<Void>

    // 👤 GET MEMBERS OF GROUP
    @GET("group-members/{groupId}")
    fun getMembers(@Path("groupId") groupId: Long): Call<List<GroupMemberResponse>>

    // 💸 EXPENSES BY GROUP
    @GET("expenses/group/{groupId}")
    fun getExpenses(@Path("groupId") groupId: Long): Call<List<Expense>>

    // 💸 EXPENSES BY USER
    @GET("expenses/user/{userId}")
    fun getUserExpenses(@Path("userId") userId: Long): Call<List<Expense>>

    // ➕ ADD EXPENSE
    @POST("expenses")
    fun addExpense(@Body expense: Expense): Call<Expense>
}