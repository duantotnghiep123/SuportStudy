package com.example.suportstudy.service

import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ParticipantAPI {

    @GET("/api/getAllParticipant")
    fun getAllParticipant(): Call<List<Participant>>

    @POST("/api/insertParticipant")
    @FormUrlEncoded
    fun insertParticipant(
        @Field("jointime") jointime: String?,
        @Field("uid") uid: String?,
        @Field("groupId") groupId: String,
    ): Call<Participant>

}