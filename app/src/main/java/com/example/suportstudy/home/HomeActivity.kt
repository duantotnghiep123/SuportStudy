package com.example.suportstudy.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.suportstudy.MainActivity
import com.example.suportstudy.R
import com.example.suportstudy.until.Until
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
   val context=this@HomeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Realm.init(applicationContext)
        val app = App(AppConfiguration.Builder(Until.appId).build())
        var user=app.currentUser()
        if(user==null){
            Until.nextActivity(context,MainActivity::class.java)
        }

        btnLogout.setOnClickListener {
                 app.currentUser()?.logOutAsync {
                if (it.isSuccess) {
                    Log.v("AUTH", "Successfully logged out.")
                    Until.nextActivity(context,MainActivity::class.java)

                } else {
                    Log.e("AUTH", it.error.toString())
                }
            }
        }
    }
}