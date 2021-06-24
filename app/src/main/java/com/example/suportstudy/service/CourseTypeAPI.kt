package com.example.suportstudy.service

import com.example.suportstudy.model.Course
import com.example.suportstudy.model.CourseType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface CourseTypeAPI {
    @GET("/api/getAllCourseType")
    suspend fun getAllCourseType(): Response<List<CourseType>>
}