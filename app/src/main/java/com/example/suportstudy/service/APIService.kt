package com.example.suportstudy.service

import com.example.suportstudy.model.Chat
import com.example.suportstudy.model.Question
import com.example.suportstudy.until.Until
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface APIService {
    @GET("/api/allQuestion")
    suspend fun getAllQuestion(): Response<List<Question>>


    @GET("/api/allChat")
    suspend fun getAllMessage(): Response<List<Chat>>

    @POST("/api/insertChat")
    @FormUrlEncoded
    fun saveChat(
        @Field("senderUid") senderUid: String?,
        @Field("receiverUid") receiverUid: String?,
        @Field("timeSend") timeSend: String,
        @Field("messageType") messageType: String,
        @Field("message") message: String,
    ): Call<Chat>

    @GET("/allcourse")
    suspend fun getAllCourse(): Response<List<Question>>

}