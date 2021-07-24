package com.example.suportstudy.service

import com.example.suportstudy.fragment.newsfeed.NewsFeedResponse
import retrofit2.Call
import retrofit2.http.GET

interface NewsFeedAPI {
    @GET("/api/newsfeed")
    fun getAllNewsFeed(): Call<NewsFeedResponse>
}