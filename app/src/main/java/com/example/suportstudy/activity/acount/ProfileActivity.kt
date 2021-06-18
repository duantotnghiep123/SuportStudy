package com.example.suportstudy.activity.acount

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.example.suportstudy.R
import com.example.suportstudy.activity.MainActivity
import com.example.suportstudy.activity.group.ListGroupActivity
import com.example.suportstudy.until.Constrain

class ProfileActivity : AppCompatActivity() {
    val context=this@ProfileActivity
    var listGroupLayout:RelativeLayout?=null
    var logoutLayout:RelativeLayout?=null

    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        sharedPreferences = getSharedPreferences(Constrain.SHARED_REF_NAME, MODE_PRIVATE)

        listGroupLayout=findViewById(R.id.listGroupLayout)
        logoutLayout=findViewById(R.id.logoutLayout)

        listGroupLayout!!.setOnClickListener {
            var intent=Intent(context,ListGroupActivity::class.java)
            intent.putExtra("group","groupMyJoin")
            startActivity(intent)
        }
        logoutLayout!!.setOnClickListener {
            val editor = sharedPreferences!!.edit()
            editor.clear()
            editor.commit()
            startActivity(Intent(context,MainActivity::class.java))
            finish()
        }
    }
}