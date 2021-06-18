package com.example.suportstudy.activity.course

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.acount.ProfileActivity
import com.example.suportstudy.adapter.CourseAdapter
import com.example.suportstudy.model.Course
import com.example.suportstudy.service.CourseAPI
import com.example.suportstudy.until.Constrain
import kotlinx.coroutines.*

class ListCourseActivity : AppCompatActivity() {

    var courseLayout: RelativeLayout? = null
    val context = this@ListCourseActivity
    var rcvAndroidViewCourse: RecyclerView? = null
    var rcvPythonCourse: RecyclerView? = null
    var rcvRectNatieCourse: RecyclerView? = null
    var rcvCSharpCourse: RecyclerView? = null
    var courseAdapter: CourseAdapter? = null
    var IVProfile:ImageView?=null
    var listAndroid=ArrayList<Course>()
    var listPython=ArrayList<Course>()
    var listJava=ArrayList<Course>()
    var listCShap=ArrayList<Course>()
    val coursedata = MutableLiveData<List<Course>>()

    var courseAPI: CourseAPI? = null

    var lazyLoader: LazyLoader? = null
    var sharedPreferences: SharedPreferences? = null

    companion object{
        var uid:String?=null
        var name:String?=null
        var email:String?=null
        var istutor:Boolean?=null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_course)

        sharedPreferences = getSharedPreferences(Constrain.SHARED_REF_NAME, MODE_PRIVATE)

         uid = sharedPreferences!!.getString(Constrain.KEY_ID, "")
         name = sharedPreferences!!.getString(Constrain.KEY_NAME, "")
         email = sharedPreferences!!.getString(Constrain.KEY_EMAIL, "")
         istutor = sharedPreferences!!.getBoolean(Constrain.KEY_ISTUTOR, false)

        Log.d("name", name!!)


        rcvAndroidViewCourse = findViewById(R.id.rcvAndroidCourse)
        rcvPythonCourse = findViewById(R.id.rcvPythonCourse)
        rcvRectNatieCourse = findViewById(R.id.rcvRectNatieCourse)
        rcvCSharpCourse = findViewById(R.id.rcvC)
        IVProfile = findViewById(R.id.IVProfile)
        lazyLoader = findViewById(R.id.myLoader)
        courseLayout = findViewById(R.id.courseLayout)

        lazyLoader!!.visibility = View.VISIBLE
        courseLayout!!.visibility = View.GONE

        courseAPI = Constrain.createRetrofit(CourseAPI::class.java)
        loadCourse()
        IVProfile!!.setOnClickListener {
            Constrain.nextActivity(context,ProfileActivity::class.java)
//            val editor = sharedPreferences!!.edit()
//            editor.clear()
//            editor.commit()
//            startActivity(Intent(context,MainActivity::class.java))
//            finish()
        }
    }

    fun loadCourse() {
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Constrain.showToast(context, "Data error")
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            val responce = courseAPI!!.getAllCourse()
            coursedata.postValue(responce.body())
        }
        coursedata.observe(this, {
            for (i in it.indices) {
                if (it[i].courseType.name.equals("ANDROID")) {
                    listAndroid.add(it[i])
                    setAdapter(rcvAndroidViewCourse!!, listAndroid)
                }
                if (it[i].courseType.name.equals("JAVA")) {
                    listJava.add(it[i])
                    setAdapter(rcvRectNatieCourse!!, listJava)
                }
                if (it[i].courseType.name.equals("C#")) {
                    listCShap.add(it[i])
                    setAdapter(rcvCSharpCourse!!,listCShap)
                }
                if (it[i].courseType.name.equals("PYTHON")) {
                    listPython.add(it[i])
                    setAdapter(rcvPythonCourse!!,listPython)
                }
            }
            lazyLoader!!.visibility = View.GONE
            courseLayout!!.visibility = View.VISIBLE
        })
    }
    fun setAdapter(recyclerView: RecyclerView, list: List<Course>) {
        var courseAdapter = CourseAdapter(context, list)
        recyclerView!!.adapter = courseAdapter
        courseAdapter!!.notifyDataSetChanged()
    }

}