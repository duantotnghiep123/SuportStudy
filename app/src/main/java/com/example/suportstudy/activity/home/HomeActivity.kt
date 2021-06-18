package com.example.suportstudy.activity.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.fragment.ChatContainerFragment
import com.example.suportstudy.fragment.DocumentFragment
import com.example.suportstudy.fragment.HomeFragment
import com.example.suportstudy.until.Until
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {



    val fragmentHome: Fragment = HomeFragment()
    val fragmentChat: Fragment = ChatContainerFragment()
    val fragmentDocument: Fragment = DocumentFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragmentDocument
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        fm.beginTransaction().add(R.id.mainContainer, fragmentHome, "3").hide(fragmentHome).commit()
        fm.beginTransaction().add(R.id.mainContainer, fragmentChat, "2").hide(fragmentChat).commit()
        fm.beginTransaction().add(R.id.mainContainer, fragmentDocument, "1").commit()
        btnDocument.setOnClickListener {
            fm.beginTransaction().hide(active).show(fragmentDocument).commit()
            active = fragmentDocument

        }
        btnChat.setOnClickListener {
            fm.beginTransaction().hide(active).show(fragmentChat).commit()
            active = fragmentChat
        }
        btnHome.setOnClickListener {
            fm.beginTransaction().hide(active).show(fragmentHome).commit()
            active = fragmentHome
        }


    }
}