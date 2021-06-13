package com.example.suportstudy.activity.course

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.suportstudy.R
import com.example.suportstudy.activity.home.HomeActivity
import com.example.suportstudy.fragment.HomeFragment
import com.squareup.picasso.Picasso

class CourseDetailActivity : AppCompatActivity() {

    var txtCourseName:TextView?=null
    var txtDescription:TextView?=null
    var IVCourse:ImageView?=null
    var btnJoin:Button?=null

    var context=this@CourseDetailActivity

    companion object{
        var imageUrl=""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)
        txtCourseName=findViewById(R.id.txtCourseName)
        txtDescription=findViewById(R.id.txtDescription)
        IVCourse=findViewById(R.id.IVCourse)
        btnJoin=findViewById(R.id.btnJoin)
         loadDetail()
         btnJoin!!.setOnClickListener {
           var intent=Intent(context,HomeActivity::class.java)
             startActivity(intent)
         }

    }


    fun loadDetail(){
        var  intent: Intent = getIntent()
        var name=intent.getStringExtra("name")
        var desciption=intent.getStringExtra("desciption")
         imageUrl= intent.getStringExtra("image").toString()

        txtCourseName!!.text=name
        txtDescription!!.text=desciption
        if(!imageUrl.equals("")){
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_gallery_grey).into(IVCourse)
        }else{
            IVCourse!!.setImageResource(R.drawable.ic_gallery_grey)
        }



    }
}