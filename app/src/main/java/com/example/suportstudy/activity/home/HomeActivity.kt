package com.example.suportstudy.activity.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.suportstudy.R
import com.example.suportstudy.fragment.ChatContainerFragment
import com.example.suportstudy.fragment.NoteFragment
import com.example.suportstudy.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {



    val fragmentHome: Fragment = HomeFragment()
    val fragmentChat: Fragment = ChatContainerFragment()
    val fragmentNote: Fragment = NoteFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragmentHome
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        fm.beginTransaction().add(R.id.mainContainer, fragmentHome, "1").commit()
        fm.beginTransaction().add(R.id.mainContainer, fragmentChat, "2").hide(fragmentChat).commit()
        fm.beginTransaction().add(R.id.mainContainer, fragmentNote, "3").hide(fragmentNote).commit()
        btnDocument.setOnClickListener {
            fm.beginTransaction().hide(active).show(fragmentNote).commit()
            active = fragmentNote
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