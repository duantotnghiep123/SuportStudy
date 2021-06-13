package com.example.suportstudy.activity.course

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.adapter.CourseAdapter
import com.example.suportstudy.model.Course
import com.example.suportstudy.service.CourseAPI
import com.example.suportstudy.until.Until

import kotlinx.coroutines.*

class ListCourseActivity : AppCompatActivity() {

    var  courseLayout:RelativeLayout?=null
   val context=this@ListCourseActivity
    var recyclerViewCourse:RecyclerView?=null
    var rcvPythonCourse:RecyclerView?=null
    var rcvRectNatieCourse:RecyclerView?=null
    var  courseAdapter:CourseAdapter?=null

    val coursedata = MutableLiveData<List<Course>>()

    var courseAPI:CourseAPI?=null

    var lazyLoader:LazyLoader?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_course)

        recyclerViewCourse=findViewById(R.id.rcvAndroidCourse)
        rcvPythonCourse=findViewById(R.id.rcvPythonCourse)
        rcvRectNatieCourse=findViewById(R.id.rcvRectNatieCourse)
        lazyLoader=findViewById(R.id.myLoader)
        courseLayout=findViewById(R.id.courseLayout)

        lazyLoader!!.visibility=View.VISIBLE
        courseLayout!!.visibility=View.GONE


        var retrofit = Until.getClient()
        courseAPI = retrofit?.create(CourseAPI::class.java)
        loadCourse()


    }


    fun loadCourse(){

        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Until.showToast(context,"Error")
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler){
            val responce = courseAPI!!.getAllCourse()
//            Until.showToast(context,responce.body().toString())
            Log.d("data",responce.body().toString())

            coursedata.postValue(responce.body())
        }

        coursedata.observe(this, {
            courseAdapter = CourseAdapter(context, it)
            recyclerViewCourse!!.adapter = courseAdapter
            rcvPythonCourse!!.adapter = courseAdapter
            rcvRectNatieCourse!!.adapter = courseAdapter
            courseAdapter!!.notifyDataSetChanged()
            lazyLoader!!.visibility=View.GONE
            courseLayout!!.visibility=View.VISIBLE
        })
    }
}