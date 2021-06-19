package com.example.suportstudy.activity.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.activity.group.GroupInfoActivity
import com.example.suportstudy.adapter.GroupChatAdapter
import com.example.suportstudy.model.GroupChat
import com.example.suportstudy.until.Constrain
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatGroupActivity : AppCompatActivity() {
    var context=this@ChatGroupActivity

    var groupId:String?=null
    var groupCreateBy:String?=null
    var groupName:String?=null
    var groupDescription:String?=null
    var groupImage:String?=null

    var senderUid=ListCourseActivity.uid

    var groupChatImage:CircleImageView?=null
    var txtGroupName:TextView?=null
    var btnInfoGroup:ImageView?=null
    var edtMessage:EditText?=null
    var btnSend:ImageView?=null
    var chatGroup_Recyclerview:RecyclerView?=null

    var groupChatAdapter:GroupChatAdapter?=null
    var groupChatList=ArrayList<GroupChat>()

    var firebaseDatabase: FirebaseDatabase? = null
    var chatGroupRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_group)
        initDataView()
        displayMessage()

        btnSend!!.setOnClickListener {
           var message=edtMessage!!.text.toString()
            if(message.equals("")){
                Constrain.showToast(context,"Hãy nhập tin nhắn...")
            }else{
                sendMessage(message)
                Constrain.hideKeyBoard(context)
            }
            edtMessage!!.setText("")
        }
        btnInfoGroup!!.setOnClickListener {
            var intent= Intent(context,GroupInfoActivity::class.java)
            intent.putExtra("groupId",groupId)
            intent.putExtra("groupCreateBy",groupCreateBy)
            intent.putExtra("groupImage",groupImage)
            intent.putExtra("groupName",groupName)
            intent.putExtra("groupDescription",groupDescription)
            context.startActivity(intent)
        }
    }

    fun initDataView(){
        var intentGroupChat=intent
        groupId=intentGroupChat.getStringExtra("groupId")
        groupCreateBy=intentGroupChat.getStringExtra("groupCreateBy")
        groupName=intentGroupChat.getStringExtra("groupName")
        groupDescription=intentGroupChat.getStringExtra("groupDescription")
        groupImage=intentGroupChat.getStringExtra("groupImage")

        groupChatImage=findViewById(R.id.groupChatImage)
        txtGroupName=findViewById(R.id.txtGroupName)
        btnInfoGroup=findViewById(R.id.btnInfoGroup)
        edtMessage=findViewById(R.id.edtMessage)
        btnSend=findViewById(R.id.btnSend)
        chatGroup_Recyclerview=findViewById(R.id.chatGroup_Recyclerview)


        firebaseDatabase = FirebaseDatabase.getInstance("https://suportstudy-72e5e-default-rtdb.firebaseio.com/")
        chatGroupRef = firebaseDatabase!!.getReference("GroupChats")



        if(!groupImage.equals("")){
            Picasso.with(context).load(groupImage).into(groupChatImage)
        }else{
            groupChatImage!!.setImageResource(R.drawable.loginimage)
        }
        txtGroupName!!.text=groupName
    }
    private fun sendMessage(message: String) {
        var  time=System.currentTimeMillis().toString()
        var  hashMap=HashMap<String, String>()
        hashMap.put("_id", time)
        hashMap.put("senderUid", senderUid!!)
        hashMap.put("senderName", ListCourseActivity.name!!)
        hashMap.put("timeSend", time)
        hashMap.put("typeMessage", "text")
        hashMap.put("message", message)
        hashMap.put("groupId", groupId!!)

        chatGroupRef!!.child(groupId!!).child("Message").push().setValue(hashMap).addOnCompleteListener( {
            if (it.isSuccessful) {
                Constrain.showToast(context, "Gửi thành công")

            }
        })

    }
    private fun displayMessage() {
        chatGroupRef!!.child(groupId!!).child("Message")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                groupChatList.clear()
                for (ds in dataSnapshot.children) {
                    val chat: GroupChat? = ds.getValue(GroupChat::class.java)
                        groupChatList.add(chat!!)
                }
                groupChatAdapter= GroupChatAdapter(context, groupChatList)
                chatGroup_Recyclerview!!.adapter=groupChatAdapter
                chatGroup_Recyclerview!!.scrollToPosition(groupChatList.size - 1)

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}