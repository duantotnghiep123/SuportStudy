package com.example.suportstudy.until

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/uploadfile")
    fun postImage(
        @Part image: MultipartBody.Part?
    ): Call<ResponseBody>
}