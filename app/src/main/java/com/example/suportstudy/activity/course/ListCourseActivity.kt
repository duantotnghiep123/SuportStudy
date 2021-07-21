package com.example.suportstudy.activity.course

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.adapter.CourseAdapter
import com.example.suportstudy.extensions.gone
import com.example.suportstudy.extensions.visible
import com.example.suportstudy.model.Course
import com.example.suportstudy.service.CourseAPI
import com.example.suportstudy.until.Constrain
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListCourseActivity : AppCompatActivity() {

    var context = this@ListCourseActivity

    var courseTypeId = ""
    var imageCourseType = ""
    var nameCourseType = ""

    var thumbIv: RoundedImageView? = null
    var txtName: TextView? = null
    var rcvCourse: RecyclerView? = null

    var courseAPI: CourseAPI? = null
    var list = ArrayList<Course>()
    var lazyLoader: LazyLoader? = null
    var noDataLayout: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_course)
        initViewData()
        loadCrouseByCourseTypeId()

    }

    fun initViewData() {
        var intent = intent
        courseTypeId = intent.getStringExtra("_id")!!
        nameCourseType = intent.getStringExtra("name")!!
        imageCourseType = intent.getStringExtra("image")!!


        thumbIv = findViewById(R.id.thumbIv)
        txtName = findViewById(R.id.txtName)
        rcvCourse = findViewById(R.id.rcvCourse)
        courseAPI = Constrain.createRetrofit(CourseAPI::class.java)
        noDataLayout = findViewById(R.id.noDataLayout)
        lazyLoader = findViewById(R.id.myLoader)
        lazyLoader!!.visible()

        Constrain.checkShowImage(context, R.drawable.ic_gallery_grey, imageCourseType, thumbIv!!)
        txtName!!.text = nameCourseType

    }

    fun loadCrouseByCourseTypeId() {
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.IO)
        scope.launch(errorHandler) {
            courseAPI!!.getAllCourse().enqueue(object :Callback<List<Course>>{
                override fun onResponse(
                    call: Call<List<Course>>,
                    response: Response<List<Course>>
                ) {
                    if (response.isSuccessful) {
                        for (i in response.body()!!.indices) {
                            if (response.body()!![i].courseType._id.equals(courseTypeId)) {
                                list.add(response.body()!![i])
                            }
                            if (list.size == 0) {
                                noDataLayout!!.visible()
                            } else {
                                noDataLayout!!.gone()
                                var courseAdapter = CourseAdapter(context, list)
                                rcvCourse!!.adapter = courseAdapter
                                courseAdapter!!.notifyDataSetChanged()
                            }
                            lazyLoader!!.gone()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                    Log.e("Error",t.message.toString())
                }

            })

        }


    }
}