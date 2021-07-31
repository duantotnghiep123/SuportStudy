package com.example.suportstudy.fragment.newsfeed

import com.example.suportstudy.model.NewsFeed
import com.google.gson.annotations.SerializedName

data class NewsFeedResponse(
    @SerializedName("payload")
    var data: ArrayList<NewsFeed>
)