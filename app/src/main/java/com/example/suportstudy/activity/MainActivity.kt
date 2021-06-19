package com.example.suportstudy.activity

import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.suportstudy.R
import com.example.suportstudy.activity.acount.LoginAndRegisterMainActivity
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.activity.quizz.QuizzActivity
import com.example.suportstudy.until.ConnectivityReceiver
import com.example.suportstudy.until.Constrain
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var isTutor=false
    var isconected=false
    private var connectivityReceiver: ConnectivityReceiver? = null
    var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(Constrain.SHARED_REF_NAME, MODE_PRIVATE)
        val _id = sharedPreferences!!.getString(Constrain.KEY_ID,"")
        val email = sharedPreferences!!.getString(Constrain.KEY_EMAIL,"")
        val isLogin = sharedPreferences!!.getBoolean(Constrain.KEY_LOGIN, false)

        if (isLogin == true) {
            val intent = Intent(this@MainActivity, ListCourseActivity::class.java)
            startActivity(intent)
            finish()
        }

        var  intent=intent
        isconected=intent.getBooleanExtra("is",false)
        if(isconected==false){
            Constrain.showToast(applicationContext, isconected.toString())
        }else{
            Constrain.showToast(applicationContext, isconected.toString())
        }

        btnTutor.setOnClickListener {
            var intent=Intent(this@MainActivity, QuizzActivity::class.java)
            startActivity(intent)
        }
        btnMember.setOnClickListener {
            isTutor=false
            var intent=Intent(this@MainActivity, LoginAndRegisterMainActivity::class.java)
            intent.putExtra("isTutor", isTutor)
            startActivity(intent)
        }

        btnLoginMain.setOnClickListener {
            var intent=Intent(this@MainActivity, LoginAndRegisterMainActivity::class.java)
            intent.putExtra("positionRegister", 0)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        connectivityReceiver = ConnectivityReceiver(homeLayout)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilter)
    }


}