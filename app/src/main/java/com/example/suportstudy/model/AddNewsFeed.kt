package com.example.suportstudy.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class AddNewsFeed (
    @SerializedName("description")
    val description: String,
    @SerializedName("post")
    val image:  String?,
    @SerializedName("userId")
    val userId: String,
)