package com.example.suportstudy.activity.course

import android.os.Bundle
import android.text.Html
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.suportstudy.R
import kotlinx.android.synthetic.main.activity_course.*

class CourseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        searchView.setActivated(true)
        searchView.setQueryHint(Html.fromHtml("<font color = #ACACAC>" + "Tìm khóa học" + "</font>"))
        searchView.onActionViewExpanded()
        searchView.setIconified(false)
        searchView.clearFocus()
        searchView.setFocusable(false)
        val linearLayout1 = searchView.getChildAt(0) as LinearLayout
        val linearLayout2 = linearLayout1.getChildAt(2) as LinearLayout
        val linearLayout3 = linearLayout2.getChildAt(1) as LinearLayout
        val autoComplete = linearLayout3.getChildAt(0) as AutoCompleteTextView
        autoComplete.textSize = 15f
    }
}