package com.example.expensetrackermobile.api

import com.example.expensetrackermobile.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/register")
    fun register(@Body user: User): Call<User>

    @POST("auth/login")
    fun login(@Body user: User): Call<User>
}