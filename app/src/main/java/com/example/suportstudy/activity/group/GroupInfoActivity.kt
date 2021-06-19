package com.example.suportstudy.activity.group

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.suportstudy.R
import com.example.suportstudy.activity.chat.ChatGroupActivity
import com.example.suportstudy.activity.course.ListCourseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class GroupInfoActivity : AppCompatActivity() {
    var context=this@GroupInfoActivity

    companion object{
        var groupCreateBy:String?=null

    }

    var groupId:String?=null
    var groupName:String?=null
    var groupDescription:String?=null
    var groupImage:String?=null

    var groupIv:ImageView?=null
    var groupNameTv:TextView?=null
    var groupDescriptionTv:TextView?=null
    var leaveGroupTv:TextView?=null
    var changeGroupNameLayout:RelativeLayout?=null
    var viewMemberLayout:RelativeLayout?=null
    var leaveGroupLayout:RelativeLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_info)
        initViewData()

        viewMemberLayout!!.setOnClickListener {
            var intent= Intent(context, ListMemberGroupActivity::class.java)
            intent.putExtra("groupId",groupId)
            context.startActivity(intent)
        }



    }

    fun initViewData(){
        groupIv=findViewById(R.id.groupImage)
        groupNameTv=findViewById(R.id.groupNameTv)
        groupDescriptionTv=findViewById(R.id.groupDescriptionTv)
        leaveGroupTv=findViewById(R.id.leaveGroupTv)
        changeGroupNameLayout=findViewById(R.id.changeGroupNameLayout)
        viewMemberLayout=findViewById(R.id.viewMemberLayout)
        leaveGroupLayout=findViewById(R.id.leaveGroupLayout)



        var intentGroupChat=intent
        groupId=intentGroupChat.getStringExtra("groupId")
        groupCreateBy=intentGroupChat.getStringExtra("groupCreateBy")
        groupName=intentGroupChat.getStringExtra("groupName")
        groupDescription=intentGroupChat.getStringExtra("groupDescription")
        groupImage=intentGroupChat.getStringExtra("groupImage")

        if(ListCourseActivity.istutor==false){
            leaveGroupTv!!.text="Rời nhóm"
        }else{
            leaveGroupTv!!.text="Xóa nhóm"
        }
        groupNameTv!!.text=groupName
        groupDescriptionTv!!.text=groupDescription

        if(!groupImage.equals("")){
            Picasso.with(context).load(groupImage).into(groupIv)
        }else{
            groupIv!!.setImageResource(R.drawable.loginimage)
        }
    }
}