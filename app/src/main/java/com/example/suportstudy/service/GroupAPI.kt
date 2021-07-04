package com.example.suportstudy.service

import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import com.example.suportstudy.model.Users
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GroupAPI {

    @GET("/api/getAllGroup")
    fun getAllGroup(): Call<List<Group>>

    @GET("/api/getAllGroup")
   suspend fun getAllGroup2(): Response<List<Group>>


    @POST("/api/insertGroup")
    @FormUrlEncoded
    fun createGroupNoImage(
        @Field("createBy") createBy: String?,
        @Field("groupName") groupName: String,
        @Field("groupDescription") groupDescription: String,
        @Field("groupImage") groupImage: String,
        @Field("courseId") courseId: String,
    ): Call<Group>

    @POST("/api/findGroupId")
    @FormUrlEncoded
    fun getGroupById(@Field("_id") _id: String?): Call<List<Group>>

    @POST("/api/findGroupId")
    @FormUrlEncoded
    fun getGroupById2(@Field("_id") _id: String?): Response<List<Group>>



    @POST("/api/updateGroupName")
    @FormUrlEncoded
    fun updateGroupName(
        @Field("_id") _idGroup: String?,
        @Field("groupName") groupName: String,
        ): Call<Group>



    @POST("/api/deleteGroup")
    @FormUrlEncoded
    fun deleteGroup(
        @Field("_id") _idGroup: String?,
    ): Call<Group>



    @Multipart
    @POST("/api/createGroupWithImage")
    fun createGroupWithImage(
        @Part ("createBy") createBy: RequestBody?,
        @Part("groupName") groupName: RequestBody,
        @Part("groupDescription") groupDescription: RequestBody,
        @Part groupImage: MultipartBody.Part?,
        @Part("courseId") courseId: RequestBody,

    ): Call<Group>

}