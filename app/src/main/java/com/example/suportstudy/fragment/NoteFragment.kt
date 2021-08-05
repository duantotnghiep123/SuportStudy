package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.italkapp.adapter.ChatContainerAdapter
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseDetailActivity
import com.example.suportstudy.adapter.DocumentAdapter
import com.example.suportstudy.adapter.NoteAdapter
import com.example.suportstudy.adapter.NoteContainerAdapter
import com.example.suportstudy.apibodymodel.GetNoteBody
import com.example.suportstudy.databinding.FragmentNoteBinding
import com.example.suportstudy.model.Document
import com.example.suportstudy.model.Note
import com.example.suportstudy.service.QuestionAPI
import com.example.suportstudy.until.ConnectionManager
import com.example.suportstudy.until.Constrain
import com.example.suportstudy.viewmodel.NoteViewModel
import com.google.android.material.tabs.TabLayout
import org.bson.types.ObjectId
import kotlin.collections.ArrayList


class NoteFragment : Fragment() {
    companion object {

    }

    private lateinit var binding: FragmentNoteBinding

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
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        init()
//        subscribeUI()
//        subscribeUserAva()
//        getNote()
//        setDataToNoteRecyclerView()
//        Constrain.hideKeyBoard(activity as Activity)
        setUpUI()
        binding.menu.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    private fun setUpUI() {
        viewPagerChat = binding.viewpagerChat
        tabLayout = binding.tablayoutChat
        tabLayout!!.addTab(tabLayout!!.newTab())
        tabLayout!!.addTab(tabLayout!!.newTab())


        val adapter = NoteContainerAdapter(childFragmentManager)
        viewPagerChat!!.adapter = adapter
        viewPagerChat!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPagerChat!!.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}