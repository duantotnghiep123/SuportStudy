package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.italkapp.adapter.ChatContainerAdapter
import com.example.suportstudy.R
import com.google.android.material.tabs.TabLayout


class ChatContainerFragment : Fragment() {
    private var viewPagerChat: ViewPager? = null
    private var tabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_chat_container, container, false)

        viewPagerChat = view.findViewById(R.id.viewpager_chat)
        tabLayout = view.findViewById(R.id.tablayout_chat)
        tabLayout!!.addTab(tabLayout!!.newTab())
        tabLayout!!.addTab(tabLayout!!.newTab())


        val adapter = ChatContainerAdapter(childFragmentManager)
        viewPagerChat!!.setAdapter(adapter)
        viewPagerChat!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerChat!!.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatContainerFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}