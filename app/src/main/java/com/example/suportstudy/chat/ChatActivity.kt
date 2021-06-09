package com.example.suportstudy.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.adapter.ChatAdapter
import com.example.suportstudy.model.Chat
import com.example.suportstudy.retrofit.APIService
import com.example.suportstudy.until.Until
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoCollection
import io.realm.mongodb.mongo.MongoDatabase
import kotlinx.coroutines.*
import org.bson.Document
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var profileIv: ImageView? = null
    var attachBtn: ImageView? = null
    var microphoneBtn: ImageButton? = null
    var nameTv: TextView? = null
    var userStatusTv: TextView? = null
    var ChatConnectionTV: TextView? = null
    var sendBtn: ImageView? = null
    var messageEt: EditText? = null

    var sender = "60bb2947d83180639a74b72a"
    var receiver = "60bb295a00ed81b22ff291fa"

    var mongoClient: MongoClient? = null
    var mongoDatabase: MongoDatabase? = null
    var mongoCollection: MongoCollection<Document>? = null
    var user: User? = null

    var context = this@ChatActivity

    val data = MutableLiveData<List<Chat>>()

    var chatApi: APIService? = null

    var chatAdapter:ChatAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        recyclerView = findViewById(R.id.chat_Recyclerview)
        profileIv = findViewById(R.id.profileIv)
        nameTv = findViewById(R.id.nameTv)
        userStatusTv = findViewById(R.id.userStatusTv)
        sendBtn = findViewById(R.id.senBtn)
        messageEt = findViewById(R.id.messageEt)
        ChatConnectionTV = findViewById(R.id.ChatConnectionTV)
        recyclerView!!.setHasFixedSize(true)



        Realm.init(context)
        val app = App(AppConfiguration.Builder(Until.appId).build())

        user = app.currentUser()!!
        Log.d("User", user!!.id.toString())
        mongoClient = user!!.getMongoClient("mongodb-atlas")
        mongoDatabase = mongoClient!!.getDatabase("SuportStudy")
        mongoCollection = mongoDatabase!!.getCollection("chats")


        var retrofit = APIService.getClient();
        chatApi = retrofit?.create(APIService::class.java)


        readMessage2()
        sendBtn!!.setOnClickListener {
            val message = messageEt!!.getText().toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(message)) {
                Until.showToast(context, "Nhập tin nhắn")
            } else {
                sendMessage2(message)

                Until.hideKeyBoard(context)
            }

            messageEt!!.setText("")
        }

    }

    fun sendMessage2(message: String) {
        var time: String = System.currentTimeMillis().toString()
        chatApi!!.savePost(user!!.id, receiver, time, "text", message)
        .enqueue(object : Callback<Chat> {
            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                if(response.isSuccessful){
                   Until.showToast(context,"Hoan Thanh")
                }
            }
            override fun onFailure(call: Call<Chat>, t: Throwable) {
                  Log.d("Errorconected",t.message.toString())
            }
        })
        readMessage2()
    }

    fun readMessage2() {
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Toast.makeText(this, "Errorconect", Toast.LENGTH_SHORT).show()
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            val responce = chatApi!!.getAllMessage()
            Log.d("data",responce.body().toString())
            data.postValue(responce.body())
        }

        data.observe(this, {
             chatAdapter = ChatAdapter(context, it)
            recyclerView!!.layoutManager?.scrollToPosition(chatAdapter!!.itemCount - 1)
            recyclerView!!.adapter = chatAdapter
            chatAdapter!!.notifyDataSetChanged()
        })
    }

    private fun sendMessage(message: String) {
        var time: String = System.currentTimeMillis().toString()
        mongoCollection!!.insertOne(
            Document(
                "senderUid", user!!.id
            )
                .append("receiverUid", receiver)
                .append("messageType", "text")
                .append("timeSend", time)
                .append("message", message)
        ).getAsync { rsul ->
            if (rsul.isSuccess) {
                readMessage2()
            } else {
                Log.v("Data", "Error:" + rsul.error.toString())
            }
        }
    }

    fun readMessage() {
        val commentsFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
        val scope = CoroutineScope(commentsFetchJob + Dispatchers.Main)

        scope.launch(errorHandler) {
            var cursor = mongoCollection!!.find().iterator()
            var chatList = ArrayList<Chat>()
            cursor.getAsync {
                chatList.clear()
                var chat: Chat? = null
                while (it.get().hasNext()) {
                    val data = it.get().next().toJson()
                    val jsonObject = JSONObject(data)
                    val senderUid = jsonObject.getString("senderUid")
                    val receiverUid = jsonObject.getString("receiverUid")
                    val timeSend = jsonObject.getString("timeSend")
                    val messageType = jsonObject.getString("messageType")
                    val message = jsonObject.getString("message")
                    chat = Chat( senderUid, receiverUid, timeSend, messageType, message)
                    chatList.add(chat!!)
                }
                var chatAdapter = ChatAdapter(context, chatList)
                recyclerView!!.adapter = chatAdapter
                recyclerView!!.layoutManager?.scrollToPosition(chatAdapter.itemCount - 1)
                chatAdapter.notifyDataSetChanged()
            }
        }


    }



}





