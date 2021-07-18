package com.example.suportstudy.controller

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.suportstudy.model.Course
import com.example.suportstudy.model.CourseType
import com.example.suportstudy.service.CourseAPI
import com.example.suportstudy.service.CourseTypeAPI
import com.example.suportstudy.until.Constrain
import kotlinx.coroutines.*

object CourseController {
    fun getAllCourse(context: Activity): MutableLiveData<List<Course>> {
        var  courseAPI = Constrain.createRetrofit(CourseAPI::class.java)
        val coursedata = MutableLiveData<List<Course>>()
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
<<<<<<< HEAD
=======
            Constrain.showToast( "Data error")
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.IO)
        scope.launch(errorHandler) {
            val responce = courseAPI!!.getAllCourse()
            if(responce.isSuccessful){
                coursedata.postValue(responce.body())
            }
        }
        return coursedata
    }




<<<<<<< HEAD
    fun getAllCourseType(context: Activity): MutableLiveData<List<CourseType>> {
=======
    fun getAllCourseType( context: Activity): MutableLiveData<List<CourseType>> {
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
        var  courseTypeAPI = Constrain.createRetrofit(CourseTypeAPI::class.java)
        val coursedata = MutableLiveData<List<CourseType>>()
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
<<<<<<< HEAD
=======
            Constrain.showToast("Data error")
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.IO)
        scope.launch(errorHandler) {
            var responce=courseTypeAPI.getAllCourseType()
            if(responce.isSuccessful){
                coursedata.postValue(responce.body())
            }
        }
        return coursedata
    }

}