package com.example.suportstudy.service

import com.example.suportstudy.model.Course
import com.example.suportstudy.model.Question
import retrofit2.Response
import retrofit2.http.GET

interface CourseAPI {

    @GET("/api/allcourse")
    suspend fun getAllCourse(): Response<List<Course>>

}