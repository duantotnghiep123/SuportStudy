package com.example.suportstudy.service

import com.example.suportstudy.model.Users
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAPI {


    @GET("/api/getAllUser")
     fun getAllUsers(): Call<List<Users>>

    @POST("/api/register")
    @FormUrlEncoded
    fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String,
        @Field("image") image: String,
        @Field("isTurtor") isTurtor: Boolean,
    ): Call<Users>

    @POST("/api/findUserEmailPassWord")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String,
    ): Call<List<Users>>

}