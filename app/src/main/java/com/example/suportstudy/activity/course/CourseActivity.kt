package com.example.suportstudy.activity.course

import android.os.Bundle
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import com.example.suportstudy.R
import com.example.suportstudy.until.Until
import kotlinx.android.synthetic.main.activity_course.*

class CourseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        Until.searchView( searchView,"Tìm khóa học")
            .setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Until.showToast(applicationContext,query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                Until.showToast(applicationContext,newText)

                return false
            }
        })
    }
}