package com.example.suportstudy.activity.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.adapter.ListMemberGroupAdapter
import com.example.suportstudy.model.Participant
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.ParticipantAPI
import com.example.suportstudy.service.UserAPI
import com.example.suportstudy.until.Constrain
import com.google.gson.annotations.Until
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMemberGroupActivity : AppCompatActivity() {
    var context=this@ListMemberGroupActivity
    var recyclerViewUser:RecyclerView?=null
    var listUsers:ArrayList<Users> = ArrayList<Users>()

    var participantAPI:ParticipantAPI?=null
    var userAPI:UserAPI?=null

    var groupId:String?=null
    var listMemberGroupAdapter:ListMemberGroupAdapter?=null
    var lazyLoader:LazyLoader?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_member_group)
        initDataView()
        getAllParticipantByGroupId()
    }

    fun initDataView(){
        lazyLoader=findViewById(R.id.myLoader)

        participantAPI=Constrain.createRetrofit(ParticipantAPI::class.java)
        userAPI=Constrain.createRetrofit(UserAPI::class.java)
        recyclerViewUser=findViewById(R.id.recyclerViewUser);
        var intentGroupChat=intent
        groupId=intentGroupChat.getStringExtra("groupId")
    }
    fun getAllParticipantByGroupId(){
        lazyLoader!!.visibility=View.VISIBLE

        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Constrain.showToast("Data error")
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            participantAPI!!.getAllParticipant()
                .enqueue(object :Callback<List<Participant>>{
                    override fun onResponse(
                        call: Call<List<Participant>>,
                        response: Response<List<Participant>>
                    ) {
                        var listP=response.body()
                        for (i in listP!!.indices){
                            if(listP[i].groupId.equals(groupId)){
                                var uid=listP[i].uid

                                getUserById(uid)
                                lazyLoader!!.visibility=View.GONE
                            }
                        }

                    }
                    override fun onFailure(call: Call<List<Participant>>, t: Throwable) {

                    }

                })
        }


    }
    fun getUserById(uid:String){
        userAPI!!.getAllUsersByID(uid)
            .enqueue(object :Callback<List<Users>>{
                override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                   if (response.isSuccessful){
                       listUsers.addAll(response.body()!!)
                   }
                    listMemberGroupAdapter= ListMemberGroupAdapter(context,listUsers)
                    recyclerViewUser!!.adapter=listMemberGroupAdapter
                }
                override fun onFailure(call: Call<List<Users>>, t: Throwable) {

                }

            })
    }
}