package com.example.suportstudy.service

import com.example.suportstudy.fragment.newsfeed.NewsFeedResponse
import com.example.suportstudy.model.AddLike
import com.example.suportstudy.model.AddNewsFeed
import com.example.suportstudy.model.GroupCourse
import com.example.suportstudy.model.NewsFeed
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NewsFeedAPI {
    @GET("/api/newsfeed")
    fun getAllNewsFeed(): Call<NewsFeedResponse>

    @POST("/api/newsfeed/")
    @FormUrlEncoded
    fun createNewsFeedNoImage(
        @Field("description") description: String,
        @Field("userId") userId: String,
        @Field("image") image: String,
    ): Call<AddNewsFeed>

    @POST("/api/newsfeed/like/id")
    @FormUrlEncoded
    fun addLike(
        @Field("like") like: Boolean,
        @Field("userId") userId: String,
        @Field("postId") postId: String,
    ): Call<AddLike>

    @Multipart
    @POST("/api/newsfeed/")
    fun createNewsFeedWithImage(
        @Part("description") description: RequestBody??,
        @Part("userId") userId: RequestBody??,
        @Part image: MultipartBody.Part?,
        ): Call<AddNewsFeed>
}