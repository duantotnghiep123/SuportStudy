package com.example.suportstudy.activity.course

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.ActionActivity
import com.example.suportstudy.adapter.CourseTypeAdapter
import com.example.suportstudy.extensions.gone
import com.example.suportstudy.extensions.onClick
import com.example.suportstudy.extensions.visible
import com.example.suportstudy.model.CourseType
import com.example.suportstudy.service.CourseTypeAPI
import com.example.suportstudy.until.ConnectionManager
import com.example.suportstudy.until.Constrain
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.makeramen.roundedimageview.RoundedImageView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseTypeActivity : AppCompatActivity()
     {
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
        getReference()
        setContentView(R.layout.activity_course_type)
        initviewData()

        backIv.setOnClickListener {
            Constrain.nextActivity(context,ActionActivity::class.java)
            finish()
        }

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.equals("")) {
                    thumbIv!!.visible()
                    getAllCourseType()
                } else {
                    thumbIv!!.gone()

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

    fun initviewData() {
        Constrain.context = context
        courseTypeAPI = Constrain.createRetrofit(CourseTypeAPI::class.java)
        rcvCourse = findViewById(R.id.rcvCourse)

        searchView = findViewById(R.id.searchView)
        backIv = findViewById(R.id.backIv)
        thumbIv = findViewById(R.id.thumbIv)

        loader = findViewById(R.id.myLoader)
        noDataLayout = findViewById(R.id.noDataLayout)
        dataLayout = findViewById(R.id.dataLayout)
        refreshLayout = findViewById(R.id.refreshLayout)
        noInternetLayout = findViewById(R.id.noInternetLayout)
        btnSetting = findViewById(R.id.btnSetting)
        loader!!.visibility = View.VISIBLE
        database = Constrain.initFirebase("users")

        try {
            var networkContion = ConnectionManager(context)
            networkContion.observe(context, { isConeted ->
                if (isConeted) {
                    noInternetLayout.gone()
                    dataLayout.visible()
                } else {
                    noInternetLayout.visible()
                    dataLayout.gone()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        btnSetting.onClick {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(Intent(android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
            }
        }
        refreshData()
        getAllCourseType()
        updateToken(FirebaseInstanceId.getInstance().getToken())


    }

    fun getReference() {
        userSharedPreferences = getSharedPreferences(Constrain.SHARED_REF_USER, MODE_PRIVATE)
        uid = userSharedPreferences!!.getString(Constrain.KEY_ID, "")
        name = userSharedPreferences!!.getString(Constrain.KEY_NAME, "")
        image = userSharedPreferences!!.getString(Constrain.KEY_IMAGE, "noImage")
        istutor = userSharedPreferences!!.getBoolean(Constrain.KEY_ISTUTOR, false)
    }

    fun getAllCourseType() {
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.IO)
        scope.launch(errorHandler) {
         courseTypeAPI.getAllCourseType().enqueue(object :Callback<List<CourseType>>{
                override fun onResponse(
                    call: Call<List<CourseType>>,
                    response: Response<List<CourseType>>
                ) {
                    if(response.isSuccessful){
                        if (response.body()!!.size == 0) {
                            loader.gone()
                            noDataLayout.visible()

                        } else {
                            var categorieAdapter = CourseTypeAdapter(context, response.body()!!)
                            rcvCourse!!.adapter = categorieAdapter
                            categorieAdapter.notifyDataSetChanged()
                            loader.gone()
                            noDataLayout.gone()
                        }
                        loader.gone()
                    }                }

                override fun onFailure(call: Call<List<CourseType>>, t: Throwable) {
                    Log.e("error",t.message.toString())
                }

            })

        }


    }

    fun searchCourse(query: String) {
        var listSearch = ArrayList<CourseType>()
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.IO)
        scope.launch(errorHandler) {
            courseTypeAPI.getAllCourseType().enqueue(object :Callback<List<CourseType>>{
                override fun onResponse(
                    call: Call<List<CourseType>>,
                    response: Response<List<CourseType>>
                ) {
                    if(response.isSuccessful){
                        if (response.body()!!.size == 0) {
                            loader.visible()

                        } else {
                            var it=response.body()
                            for (i in it!!.indices) {
                                if (it[i].name.contains(query)) {
                                    listSearch.add(it[i])
                                    break
                                }
                            }
                            var categorieAdapter = CourseTypeAdapter(context, listSearch)
                            rcvCourse!!.adapter = categorieAdapter
                            categorieAdapter.notifyDataSetChanged()

                        }
                        loader.gone()
                    }
                }

                override fun onFailure(call: Call<List<CourseType>>, t: Throwable) {
                    Log.e("error",t.message.toString())
                }

            })

        }

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



}