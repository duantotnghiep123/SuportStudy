package com.example.suportstudy.activity.chat

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.adapter.ChatOneAdapter
import com.example.suportstudy.model.Chat
import com.example.suportstudy.service.ChatAPI
import com.example.suportstudy.until.Constrain
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChatOneActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var avatarIv: ImageView? = null

    var txtName: TextView? = null
    var ChatConnectionTV: TextView? = null
    var sendBtn: ImageView? = null
    var messageEt: EditText? = null

    var senderUid = ListCourseActivity.uid
    var senderName = ListCourseActivity.name
    var receiverUid:String?=null
    var hisImage :String?=null
    var hisName :String?=null



    var firebaseDatabase: FirebaseDatabase? = null
    var chatRef: DatabaseReference? = null

    var context = this@ChatOneActivity

    val data = MutableLiveData<List<Chat>>()

    var chatList=ArrayList<Chat>()

    var chatApi: ChatAPI? = null

    var chatAdapter:ChatOneAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_one)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initDataView()

        displayMessage()

        sendBtn!!.setOnClickListener {
            val message = messageEt!!.getText().toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(message)) {
                Constrain.showToast(context, "Nhập tin nhắn")
            } else {

               sendMessage(message)
            }

            messageEt!!.setText("")
        }

    }
fun initDataView(){

    var intentChat=intent
    receiverUid=intentChat.getStringExtra("uid")
    hisImage=intentChat.getStringExtra("image")
    hisName=intentChat.getStringExtra("name")


    recyclerView = findViewById(R.id.chat_Recyclerview)
    avatarIv = findViewById(R.id.avatarIv)
    txtName = findViewById(R.id.txtName)

    sendBtn = findViewById(R.id.senBtn)
    messageEt = findViewById(R.id.messageEt)
    ChatConnectionTV = findViewById(R.id.ChatConnectionTV)
    recyclerView!!.setHasFixedSize(true)

    txtName!!.text=hisName
    if(!hisImage.equals("")){
        Picasso.with(context).load(hisName).into(avatarIv)
    }else{
        avatarIv!!.setImageResource(R.drawable.loginimage)
    }


    firebaseDatabase = FirebaseDatabase.getInstance("https://suportstudy-72e5e-default-rtdb.firebaseio.com/")
    chatRef = firebaseDatabase!!.getReference("Chats")
}
    private fun displayMessage() {
        chatRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatList.clear()
                for (ds in dataSnapshot.children) {
                    val chat: Chat? = ds.getValue(Chat::class.java)
                    chatList.add(chat!!)
                }
                chatAdapter= ChatOneAdapter(context, chatList)
                recyclerView!!.adapter=chatAdapter
                recyclerView!!.scrollToPosition(chatList.size - 1)

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun sendMessage(message: String) {
        var  time=System.currentTimeMillis().toString()
        var  hashMap=HashMap<String, String>()
        hashMap.put("_id", time)
        hashMap.put("senderUid", senderUid!!)
        hashMap.put("senderName", senderName!!)
        hashMap.put("receiverUid", receiverUid!!)
        hashMap.put("timeSend", time)
        hashMap.put("messageType", "text")
        hashMap.put("message", message)
        chatRef!!.push().setValue(hashMap).addOnCompleteListener( {
            if (!it.isSuccessful) {
                Constrain.showToast(context, "Gửi thành công")
            }
        })

    }



}





