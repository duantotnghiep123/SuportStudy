package com.example.suportstudy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseTypeActivity
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.adapter.AdapterOneChatlist
import com.example.suportstudy.model.Chat
import com.example.suportstudy.model.Chatlist
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.UserAPI
import com.example.suportstudy.until.Constrain
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatOneFragment : Fragment() {
var  rcvChatOne:RecyclerView?=null


    var chatlistList: ArrayList<Chatlist>? = null
    var userList: ArrayList<Users>? = null
    var reference: DatabaseReference? = null

    var noMessageLayout: LinearLayout? = null

    var myUid= CourseTypeActivity.uid

    var adapterOneChatlist:AdapterOneChatlist?=null

    var userAPI:UserAPI?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_chat_one, container, false)
        initViewData(view)
        // Inflate the layout for this fragment
        return view
    }
    fun initViewData(view: View){
        rcvChatOne=view.findViewById(R.id.rcvChatOne)
        userAPI=Constrain.createRetrofit(UserAPI::class.java)

        chatlistList=ArrayList<Chatlist>()
        userList=ArrayList<Users>()
        reference =
        FirebaseDatabase.getInstance().getReference("ChatList").child(myUid!!)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatlistList!!.clear()
                for (ds in dataSnapshot.children) {
                    val chatlist: Chatlist? = ds.getValue(Chatlist::class.java)
                    chatlistList!!.add(chatlist!!)
                }

                loadChats()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
    private fun loadChats() {
        var listUser:List<Users>
        userAPI!!.getAllUsers()
            .enqueue(object :Callback<List<Users>>{
                override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                    if(response.isSuccessful){
                        listUser= response.body()!!

                        for (i in listUser.indices){
                            var id=listUser[i]._id

                            for (j in chatlistList!!.indices){
                                if (id.equals(chatlistList!![j].id)) {
                                    userList!!.add(listUser[i])
                                    break
                                }
                            }
                        }

                        adapterOneChatlist =
                            AdapterOneChatlist(
                                context!!, userList!!
                            )
                        rcvChatOne!!.adapter = adapterOneChatlist
                        adapterOneChatlist!!.notifyDataSetChanged()

                        for (i in userList!!.indices) {
                            lastMessage(userList!![i]._id) // id  dzAZNw7EBJchnky8eJnrFepjBU73
                        }

                    }
                }

                override fun onFailure(call: Call<List<Users>>, t: Throwable) {

                }

            })


    }
    private fun lastMessage(userId: String) { // id  dzAZNw7EBJchnky8eJnrFepjBU73
        val reference = FirebaseDatabase.getInstance(Constrain.firebaseUrl).getReference("Chats")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var theLastMessage = "default"
                for (ds in dataSnapshot.children) {
                    val chat = ds.getValue(Chat::class.java) ?: continue
                    val sender: String = chat.senderUid!!
                    val receiver: String = chat.receiverUid!!
                    if (sender == null || receiver == null) {
                        continue
                    }
                    var i = 0
                    if (chat.receiverUid.equals(myUid) &&
                        chat.senderUid.equals(userId) ||
                        chat.receiverUid.equals(userId) &&
                        chat.senderUid.equals(myUid)
                    ) {
                        i++

                        if (chat.messageType.equals("image")) {
                            theLastMessage = "Sent a photo"
                        }
                        if (chat.messageType.equals("text")) {
                            theLastMessage = chat.message!!
                        }
                    }
                }
                adapterOneChatlist!!.setLastMessageMap(
                    userId,
                    theLastMessage
                )
                adapterOneChatlist!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatOneFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}