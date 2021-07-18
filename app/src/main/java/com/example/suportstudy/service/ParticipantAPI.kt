package com.example.suportstudy.service

import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ParticipantAPI {

    @GET("/api/getAllParticipant")
    fun getAllParticipant(): Call<List<Participant>>

    @GET("/api/getAllParticipant")
   suspend fun getAllParticipant2(): Response<List<Participant>>

    @POST("/api/insertParticipant")
    @FormUrlEncoded
    fun insertParticipant(
        @Field("jointime") jointime: String?,
        @Field("uid") uid: String?,
        @Field("groupId") groupId: String,
        @Field("courseId") courseId: String,
    ): Call<Participant>

    @POST("/api/leaveGroup")
    @FormUrlEncoded
    fun leaveGroup(
        @Field("_id") _idParticipant: String?,
    ): Call<Participant>

}