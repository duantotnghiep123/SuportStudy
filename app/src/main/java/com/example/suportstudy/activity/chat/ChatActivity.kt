package com.example.suportstudy.activity.chat

import android.os.Bundle
import android.text.TextUtils
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.adapter.ChatAdapter
import com.example.suportstudy.model.Chat
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.ChatAPI
import com.example.suportstudy.until.Until
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChatActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var profileIv: ImageView? = null

    var ChatConnectionTV: TextView? = null
    var sendBtn: ImageView? = null
    var messageEt: EditText? = null

    var sender = "60c5527bd45d2165ec51fd6f"
    var receiver = "60c4c4d90516fb3d980e2d8c"



    var firebaseDatabase: FirebaseDatabase? = null
    var chatRef: DatabaseReference? = null

    var context = this@ChatActivity

    val data = MutableLiveData<List<Chat>>()

    var chatList=ArrayList<Chat>()

    var chatApi: ChatAPI? = null

    var chatAdapter:ChatAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        recyclerView = findViewById(R.id.chat_Recyclerview)
        profileIv = findViewById(R.id.profileIv)

        sendBtn = findViewById(R.id.senBtn)
        messageEt = findViewById(R.id.messageEt)
        ChatConnectionTV = findViewById(R.id.ChatConnectionTV)
        recyclerView!!.setHasFixedSize(true)


        firebaseDatabase = FirebaseDatabase.getInstance("https://suportstudy-72e5e-default-rtdb.firebaseio.com/")
        chatRef = firebaseDatabase!!.getReference("Chats")

        displayMessage()





        sendBtn!!.setOnClickListener {
            val message = messageEt!!.getText().toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(message)) {
                Until.showToast(context, "Nhập tin nhắn")
            } else {

               sendMessage(message)
            }

            messageEt!!.setText("")
        }

    }

    private fun displayMessage() {
        chatRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatList.clear()
                for (ds in dataSnapshot.children) {
                    val chat: Chat? = ds.getValue(Chat::class.java)
                    chatList.add(chat!!)
                }
                chatAdapter= ChatAdapter(context, chatList)
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
        hashMap.put("senderUid", sender)
        hashMap.put("receiverUid", receiver)
        hashMap.put("timeSend", time)
        hashMap.put("messageType", "text")
        hashMap.put("message", message)
        chatRef!!.push().setValue(hashMap).addOnCompleteListener( {
            if (!it.isSuccessful) {
                Until.showToast(context, "Gửi thành công")
            }
        })

    }

//    fun sendMessage2(message: String) {
//        var time: String = System.currentTimeMillis().toString()
//        chatApi!!.saveChat(user!!.id, receiver, time, "text", message)
//        .enqueue(object : Callback<Chat> {
//            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
//                if (response.isSuccessful) {
//                    Until.showToast(context, "Hoan Thanh")
//                }
//            }
//
//            override fun onFailure(call: Call<Chat>, t: Throwable) {
//                Log.d("Errorconected", t.message.toString())
//            }
//        })
//        readMessage2()
//    }
//
//    fun readMessage2() {
//        val chatFetchJob = Job()
//        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
//            throwable.printStackTrace()
//            Toast.makeText(this, "Errorconect", Toast.LENGTH_SHORT).show()
//        }
//        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
//        scope.launch(errorHandler) {
//            val responce = chatApi!!.getAllMessage()
//            Log.d("data", responce.body().toString())
//            data.postValue(responce.body())
//        }
//
//        data.observe(this, {
//            chatAdapter = ChatAdapter(context, it)
//            recyclerView!!.layoutManager?.scrollToPosition(chatAdapter!!.itemCount - 1)
//            recyclerView!!.adapter = chatAdapter
//            chatAdapter!!.notifyDataSetChanged()
//        })
//    }
//
//    private fun sendMessage(message: String) {
//        var time: String = System.currentTimeMillis().toString()
//        mongoCollection!!.insertOne(
//            Document(
//                "senderUid", user!!.id
//            )
//                .append("receiverUid", receiver)
//                .append("messageType", "text")
//                .append("timeSend", time)
//                .append("message", message)
//        ).getAsync { rsul ->
//            if (rsul.isSuccess) {
//                readMessage2()
//            } else {
//                Log.v("Data", "Error:" + rsul.error.toString())
//            }
//        }
//    }
//
//    fun readMessage() {
//        val commentsFetchJob = Job()
//        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
//            throwable.printStackTrace()
//            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
//        }
//        val scope = CoroutineScope(commentsFetchJob + Dispatchers.Main)
//
//        scope.launch(errorHandler) {
//            var cursor = mongoCollection!!.find().iterator()
//            var chatList = ArrayList<Chat>()
//            cursor.getAsync {
//                chatList.clear()
//                var chat: Chat? = null
//                while (it.get().hasNext()) {
//                    val data = it.get().next().toJson()
//                    val jsonObject = JSONObject(data)
//                    val senderUid = jsonObject.getString("senderUid")
//                    val receiverUid = jsonObject.getString("receiverUid")
//                    val timeSend = jsonObject.getString("timeSend")
//                    val messageType = jsonObject.getString("messageType")
//                    val message = jsonObject.getString("message")
//                    chat = Chat(senderUid, receiverUid, timeSend, messageType, message)
//                    chatList.add(chat!!)
//                }
//                var chatAdapter = ChatAdapter(context, chatList)
//                recyclerView!!.adapter = chatAdapter
//                recyclerView!!.layoutManager?.scrollToPosition(chatAdapter.itemCount - 1)
//                chatAdapter.notifyDataSetChanged()
//            }
//        }
//
//
//    }



}





