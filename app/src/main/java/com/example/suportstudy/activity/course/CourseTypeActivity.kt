package com.example.suportstudy.activity.course

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
<<<<<<< HEAD
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
=======
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.agrawalsuneet.dotsloader.loaders.CircularDotsLoader
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.ActionActivity
import com.example.suportstudy.activity.MainActivity
import com.example.suportstudy.activity.acount.ProfileActivity
import com.example.suportstudy.activity.call.IncomingCallActivity
import com.example.suportstudy.adapter.CourseTypeAdapter
import com.example.suportstudy.call_api.Common
import com.example.suportstudy.call_api.GenAccessToken
<<<<<<< HEAD
=======
import com.example.suportstudy.call_api.Utils
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
import com.example.suportstudy.controller.CourseController
import com.example.suportstudy.controller.UserController
import com.example.suportstudy.extensions.gone
import com.example.suportstudy.extensions.onClick
import com.example.suportstudy.extensions.visible
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
<<<<<<< HEAD

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


=======

class CourseTypeActivity : AppCompatActivity(),android.view.View.OnClickListener, LifecycleObserver {
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

        var client: StringeeClient? = null

    }
    var userSharedPreferences: SharedPreferences? = null
    var database:DatabaseReference?=null

    //call

    var uID = ""
    var tokenBase = ""
    private var sharedPreferences: SharedPreferences? = null
    var token: String? = null
    private var editor: SharedPreferences.Editor? = null
    private val PREF_NAME = "com.example.suportstudy"
    private val IS_TOKEN_REGISTERED = "is_token_registered"
    private val TOKEN = "token"

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        Log.d("AppLifecycle", "App in background")
        Common.isAppInBackground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Log.d("AppLifecycle", "App in foreground")
        Common.isAppInBackground = false
    }
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getReference()
        setContentView(R.layout.activity_course_type)
<<<<<<< HEAD
        initviewData()

        backIv.setOnClickListener {
            Constrain.nextActivity(context,ActionActivity::class.java)
            finish()
=======

        initview()
        avatarIv!!.setOnClickListener {
            Constrain.nextActivity(context, ProfileActivity::class.java)
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
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
<<<<<<< HEAD
        }
        refreshData()
        getAllCourseType()
=======
        })


        courseTypeAPI=Constrain.createRetrofit(CourseTypeAPI::class.java)
        rcvCourse=findViewById(R.id.rcvCourse)
        avatarIv=findViewById(R.id.avatarIv)
        searchView=findViewById(R.id.searchView)
        thumbIv=findViewById(R.id.thumbIv)
        lazyLoader=findViewById(R.id.myLoader)
        noDataLayout=findViewById(R.id.noDataLayout)
        userSharedPreferences = getSharedPreferences(Constrain.SHARED_REF_USER, MODE_PRIVATE)
        lazyLoader!!.visibility=View.VISIBLE

        database=Constrain.initFirebase("users")

        uid = userSharedPreferences!!.getString(Constrain.KEY_ID, "")
        name = userSharedPreferences!!.getString(Constrain.KEY_NAME, "")
        email = userSharedPreferences!!.getString(Constrain.KEY_EMAIL, "")
        image = userSharedPreferences!!.getString(Constrain.KEY_IMAGE, "noImage")
        istutor = userSharedPreferences!!.getBoolean(Constrain.KEY_ISTUTOR, false)


        token = GenAccessToken.genAccessToken(uid)

>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
        updateToken(FirebaseInstanceId.getInstance().getToken())
        setupNotification()


    }

    fun getReference() {
        userSharedPreferences = getSharedPreferences(Constrain.SHARED_REF_USER, MODE_PRIVATE)
        uid = userSharedPreferences!!.getString(Constrain.KEY_ID, "")
        name = userSharedPreferences!!.getString(Constrain.KEY_NAME, "")
        image = userSharedPreferences!!.getString(Constrain.KEY_IMAGE, "noImage")
        istutor = userSharedPreferences!!.getBoolean(Constrain.KEY_ISTUTOR, false)
    }

    fun getAllCourseType() {
        CourseController.getAllCourseType(context).observe(context, {
            if (it.size == 0) {
                loader.visible()

            } else {
                var categorieAdapter = CourseTypeAdapter(context, it)
                rcvCourse!!.adapter = categorieAdapter
                categorieAdapter.notifyDataSetChanged()
                loader.gone()

            }
            loader.gone()

        })
    }

    fun searchCourse(query: String) {
        var listSearch = ArrayList<CourseType>()
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

<<<<<<< HEAD
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

=======
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
    fun updateToken(token: String?) {
        val ref = FirebaseDatabase.getInstance(Constrain.firebaseUrl).getReference("Tokens")
        ref.child(uid!!).child("token").setValue(token)
    }

    fun initAndConnectStringee() {
        client = StringeeClient(this)
        client!!.setConnectionListener(object : StringeeConnectionListener {
            override fun onConnectionConnected(
                stringeeClient: StringeeClient,
                isReconnecting: Boolean
            ) {
                val isTokenRegistered = sharedPreferences!!.getBoolean(IS_TOKEN_REGISTERED, false)
                if (!isTokenRegistered) {
                    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.d("Stringee", "getInstanceId failed", task.exception)
                                return@OnCompleteListener
                            }
                            tokenBase = task.result!!.token
                            client!!.registerPushToken(
                                tokenBase,
                                object : StatusListener() {
                                    override fun onSuccess() {
                                        database!!.child(uid!!).child("token")
                                            .setValue(tokenBase)
                                        editor!!.putBoolean(IS_TOKEN_REGISTERED, true)
                                        editor!!.putString(TOKEN, token)
                                        editor!!.commit()
                                    }

                                    override fun onError(error: StringeeError) {
                                        Log.d(
                                            "Stringee",
                                            "Register push token unsuccessfully: " + error.getMessage()
                                        )
                                    }
                                })
                        })
                }
            }

            override fun onConnectionDisconnected(
                stringeeClient: StringeeClient,
                isReconnecting: Boolean
            ) {
                runOnUiThread {
                    //                        Utils.reportMessage(MainActivity.this, "Bị mất kết nối");
                }
            }

            override fun onIncomingCall(stringeeCall: StringeeCall) {
                if (Common.isInCall) {
                    stringeeCall.hangup()
                } else {
                    Common.callsMap.put(stringeeCall.callId, stringeeCall)
                    val intent = Intent(context, IncomingCallActivity::class.java)
                    intent.putExtra("call_id", stringeeCall.callId)
                    startActivity(intent)
                }
            }

            override fun onIncomingCall2(stringeeCall2: StringeeCall2) {}
            override fun onConnectionError(
                stringeeClient: StringeeClient,
                stringeeError: StringeeError
            ) {
                Log.d("Stringee", "StringeeClient fails to connect: " + stringeeError.getMessage())
                runOnUiThread {
                    Utils.reportMessage(
                        context,
                        "Lỗi kết nối: " + stringeeError.getMessage()
                    )
                }
            }

            override fun onRequestNewToken(stringeeClient: StringeeClient) {
                // Get new token here and connect to Stringe server
            }

            override fun onCustomMessage(s: String, jsonObject: JSONObject) {}
            override fun onTopicMessage(s: String, jsonObject: JSONObject) {}
        })
        client!!.connect(token)
    }
    private fun setupNotification() {
        requiredPermissions()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nm?.cancel(44448888)
        initAndConnectStringee()
    }

    private fun requiredPermissions() {
        ActivityCompat.requestPermissions(
            context, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ), 1
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(
                    this,
                    "Vui lòng cấp quyền để thực hiện Video Call!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //Block
    override fun onBackPressed() {}
    override fun onClick(v: View?) {

    }

    override fun onClick(v: View?) {

    }

}