package com.example.suportstudy.activity.course

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.acount.ProfileActivity
import com.example.suportstudy.adapter.CourseTypeAdapter
import com.example.suportstudy.controller.CourseController
import com.example.suportstudy.model.CourseType
import com.example.suportstudy.service.CourseTypeAPI
import com.example.suportstudy.until.ConnectionManager
import com.example.suportstudy.until.Constrain
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.makeramen.roundedimageview.RoundedImageView
import de.hdodenhof.circleimageview.CircleImageView

class CourseTypeActivity : AppCompatActivity() {
    var context=this@CourseTypeActivity

    var list:List<CourseType>?=null
    var avatarIv: CircleImageView?=null
    var thumbIv: RoundedImageView?=null
    var rcvCourse: RecyclerView?=null
    var searchView: SearchView?=null
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
       getAllCourseType()
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.equals("")) {
                    thumbIv!!.visibility = View.VISIBLE
                    getAllCourseType()
                } else {
                    thumbIv!!.visibility = View.GONE

                    searchCourse(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.equals("")) {
                    thumbIv!!.visibility = View.VISIBLE
                    getAllCourseType()
                } else {
                    thumbIv!!.visibility = View.GONE
                    searchCourse(newText)
                }
                return false
            }
        })

    }

    fun initview(){
     val dialog=  Constrain.createDialog(context, R.layout.dialog_no_internet)
        var networkContion=ConnectionManager(context)
        networkContion.observe(context, { isConeted ->
            if (isConeted) {
                dialog.dismiss()
            } else {
                dialog.show()
            }
        })


        courseTypeAPI=Constrain.createRetrofit(CourseTypeAPI::class.java)
        rcvCourse=findViewById(R.id.rcvCourse)
        avatarIv=findViewById(R.id.avatarIv)
        searchView=findViewById(R.id.searchView)
        thumbIv=findViewById(R.id.thumbIv)
        lazyLoader=findViewById(R.id.myLoader)
        noDataLayout=findViewById(R.id.noDataLayout)
        sharedPreferences = getSharedPreferences(Constrain.SHARED_REF_NAME, MODE_PRIVATE)
        lazyLoader!!.visibility=View.VISIBLE

        uid = sharedPreferences!!.getString(Constrain.KEY_ID, "")
        name = sharedPreferences!!.getString(Constrain.KEY_NAME, "")
        email = sharedPreferences!!.getString(Constrain.KEY_EMAIL, "")
        image = sharedPreferences!!.getString(Constrain.KEY_IMAGE, "noImage")
        istutor = sharedPreferences!!.getBoolean(Constrain.KEY_ISTUTOR, false)
        updateToken(FirebaseInstanceId.getInstance().getToken())

        Log.d("name", "_id : " + uid + " , name : " + name!! + " , image :" + image)

    }
    fun getAllCourseType(){
        CourseController.getAllCourseType(context).observe(context, {
            if (it.size == 0) {
                lazyLoader!!.visibility = View.VISIBLE

            } else {
                lazyLoader!!.visibility = View.GONE
                var categorieAdapter = CourseTypeAdapter(context, it)
                rcvCourse!!.adapter = categorieAdapter
                categorieAdapter.notifyDataSetChanged()
            }
            lazyLoader!!.visibility = View.GONE

        })
    }
    fun searchCourse(query: String){
        var listSearch=ArrayList<CourseType>()
        CourseController.getAllCourseType(context).observe(context, {
            for (i in it.indices) {
                if (it[i].name.contains(query)) {
                    listSearch.add(it[i])
                    break
                }
            }
            var categorieAdapter = CourseTypeAdapter(context, listSearch)
            rcvCourse!!.adapter = categorieAdapter
            categorieAdapter.notifyDataSetChanged()


        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    fun updateToken(token: String?) {
        val ref = FirebaseDatabase.getInstance(Constrain.firebaseUrl).getReference("Tokens")
        ref.child(uid!!).child("token").setValue(token)
    }


}