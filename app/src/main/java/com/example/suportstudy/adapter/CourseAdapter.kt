package com.example.suportstudy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseDetailActivity
import com.example.suportstudy.model.Chat
import com.example.suportstudy.model.Course
import com.squareup.picasso.Picasso

class CourseAdapter(var context: Context,var courseList: List<Course>) :RecyclerView.Adapter<CourseAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var IVCourse: ImageView? = null
        var txtCourseName: TextView? = null
        init {
            IVCourse = itemView.findViewById(R.id.IVCourse)
            txtCourseName = itemView.findViewById(R.id.txtCourseName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_course, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var course=courseList[position]
        var name=course.name
        var desciption=course.description
        var imageUrl=course.image
        if(!imageUrl.equals("")){
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_gallery_grey).into(holder.IVCourse)
        }else{
            holder.IVCourse!!.setImageResource(R.drawable.ic_gallery_grey)
        }
        holder.txtCourseName!!.text=name

        holder.itemView.setOnClickListener {
            var  intent:Intent= Intent(context,CourseDetailActivity::class.java)
            intent.putExtra("courseId",course._id)
            intent.putExtra("name",name)
            intent.putExtra("desciption",desciption)
            intent.putExtra("image",imageUrl)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }
}