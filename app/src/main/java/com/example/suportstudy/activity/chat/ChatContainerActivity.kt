package com.example.suportstudy.activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.suportstudy.R
import com.example.suportstudy.adapter.ChatContainerAdapter
import com.google.android.material.tabs.TabLayout

class ChatContainerActivity: AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_container)
        viewPager = findViewById(R.id.viewpager_chat)
        tabLayout = findViewById(R.id.tablayout_chat)
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Message"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Group"))
        val adapter = ChatContainerAdapter(supportFragmentManager)
        viewPager!!.setAdapter(adapter)
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.setCurrentItem(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}