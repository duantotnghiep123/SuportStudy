package com.example.suportstudy.activity.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.suportstudy.activity.MainActivity
import com.example.suportstudy.R
import com.example.suportstudy.until.Until
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
   val context=this@HomeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }
}