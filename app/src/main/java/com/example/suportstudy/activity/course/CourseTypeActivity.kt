package com.example.suportstudy.activity.course

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.acount.ProfileActivity
import com.example.suportstudy.activity.call.IncomingCallActivity
import com.example.suportstudy.adapter.CourseTypeAdapter
import com.example.suportstudy.call_api.Common
import com.example.suportstudy.call_api.GenAccessToken
import com.example.suportstudy.controller.CourseController
import com.example.suportstudy.model.CourseType
import com.example.suportstudy.service.CourseTypeAPI
import com.example.suportstudy.until.ConnectionManager
import com.example.suportstudy.until.Constrain
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.makeramen.roundedimageview.RoundedImageView
import com.stringee.StringeeClient
import com.stringee.call.StringeeCall
import com.stringee.call.StringeeCall2
import com.stringee.exception.StringeeError
import com.stringee.listener.StatusListener
import com.stringee.listener.StringeeConnectionListener
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject

class CourseTypeActivity : AppCompatActivity(), android.view.View.OnClickListener,
    LifecycleObserver {
    var context = this@CourseTypeActivity

    lateinit var list: List<CourseType>
    lateinit var avatarIv: CircleImageView
    lateinit var txtName: TextView
    lateinit var thumbIv: RoundedImageView
    lateinit var rcvCourse: RecyclerView
    lateinit var searchView: SearchView
    lateinit var backIv: ImageView
    lateinit var courseTypeAPI: CourseTypeAPI

    lateinit var loader: LazyLoader
    lateinit var noDataLayout: LinearLayout
    lateinit var dataLayout: LinearLayout
    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var noInternetLayout: LinearLayout
    lateinit var btnSetting: AppCompatButton


    var uid: String? = null
    var name: String? = null
    var image: String? = null
    var istutor: Boolean? = null
    var userSharedPreferences: SharedPreferences? = null
    var database: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_type)
        initviewData()

        backIv.setOnClickListener {
            Constrain.nextActivity(context,ActionActivity::class.java)
            finish()
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
        }
        refreshData()
        getAllCourseType()
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

    fun refreshData() {
        refreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                refreshData() // your code
                refreshLayout.setRefreshing(false)
            }

            private fun refreshData() {
                finish()
                overridePendingTransition(0, 0)
                startActivity(getIntent())
                overridePendingTransition(0, 0)
            }
        })
    }

    fun updateToken(token: String?) {
        val ref = FirebaseDatabase.getInstance(Constrain.firebaseUrl).getReference("Tokens")
        ref.child(uid!!).child("token").setValue(token)
    }


    override fun onClick(v: View?) {

    }

}