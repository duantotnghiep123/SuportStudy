package com.example.suportstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.suportstudy.authencation.LoginActivity
import com.example.suportstudy.authencation.RegisterActivity
import com.example.suportstudy.until.Until
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var isTutor=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTutor.setOnClickListener {
            var intent=Intent(this@MainActivity,QuizzActivity::class.java)
            startActivity(intent)
        }
        btnMember.setOnClickListener {
            isTutor=false
            var intent=Intent(this@MainActivity,RegisterActivity::class.java)
            intent.putExtra("isTutor",isTutor)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnCick).setOnClickListener {
            Until.nextActivity(this@MainActivity,LoginActivity::class.java)
        }
    }
}