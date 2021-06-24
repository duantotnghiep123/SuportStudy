package com.example.suportstudy.activity.course

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.acount.ProfileActivity
import com.example.suportstudy.adapter.CourseTypeAdapter
import com.example.suportstudy.controller.CourseController
import com.example.suportstudy.model.CourseType
import com.example.suportstudy.service.CourseTypeAPI
import com.example.suportstudy.until.Constrain
import de.hdodenhof.circleimageview.CircleImageView

class CourseTypeActivity : AppCompatActivity() {
    var context=this@CourseTypeActivity

    var list:List<CourseType>?=null
    var avatarIv: CircleImageView?=null
    var rcvCourse: RecyclerView?=null
    var courseTypeAPI: CourseTypeAPI?=null

    var lazyLoader:LazyLoader?=null
    var noDataLayout:LinearLayout?=null

    companion object{
        var uid:String?=null
        var name:String?=null
        var email:String?=null
        var image:String?=null
        var istutor:Boolean?=null
    }
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_type)
         initview()
        avatarIv!!.setOnClickListener {
            Constrain.nextActivity(context, ProfileActivity::class.java)
        }


        CourseController.getAllCourseType(context).observe(context,{
            if(it.size==0){
                lazyLoader!!.visibility=View.VISIBLE

            }else{
                lazyLoader!!.visibility=View.GONE
                var categorieAdapter=CourseTypeAdapter(context, it)
                rcvCourse!!.adapter=categorieAdapter
                categorieAdapter.notifyDataSetChanged()
            }

            lazyLoader!!.visibility=View.GONE

        })
    }

    fun initview(){
        courseTypeAPI=Constrain.createRetrofit(CourseTypeAPI::class.java)
        rcvCourse=findViewById(R.id.rcvCourse)
        avatarIv=findViewById(R.id.avatarIv)
        lazyLoader=findViewById(R.id.myLoader)
        noDataLayout=findViewById(R.id.noDataLayout)
        sharedPreferences = getSharedPreferences(Constrain.SHARED_REF_NAME, MODE_PRIVATE)
        lazyLoader!!.visibility=View.VISIBLE

        uid = sharedPreferences!!.getString(Constrain.KEY_ID, "")
        name = sharedPreferences!!.getString(Constrain.KEY_NAME, "")
        email = sharedPreferences!!.getString(Constrain.KEY_EMAIL, "")
        image = sharedPreferences!!.getString(Constrain.KEY_IMAGE, "noImage")
        istutor = sharedPreferences!!.getBoolean(Constrain.KEY_ISTUTOR, false)

        Log.d("name", "_id : "+ uid +" , name : " + name!! + " , image :"+ image)

    }
}