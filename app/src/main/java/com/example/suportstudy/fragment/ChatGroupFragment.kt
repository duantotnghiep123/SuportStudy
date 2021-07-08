package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseDetailActivity
import com.example.suportstudy.activity.course.CourseTypeActivity
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.adapter.GroupChatListAdapter
import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import com.example.suportstudy.service.GroupAPI
import com.example.suportstudy.service.ParticipantAPI
import com.example.suportstudy.until.Constrain
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatGroupFragment : Fragment() {

    var ref:DatabaseReference?=null
  var searchView:SearchView?=null
  var recyclerViewChatGroup:RecyclerView?=null
  var noDataLayout:LinearLayout?=null
  var myLoader:LazyLoader?=null
  var listG: ArrayList<Group>? = ArrayList<Group>()
  var listGSearch: ArrayList<Group>? = ArrayList<Group>()
  var groupAPI: GroupAPI? = null
  var participantAPI: ParticipantAPI? = null


    companion object {
        var groupChatListAdapter:GroupChatListAdapter?=null
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatGroupFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_chat_group, container, false)
        recyclerViewChatGroup=view.findViewById(R.id.recyclerViewChatGroup);
        myLoader=view.findViewById(R.id.myLoader);
        noDataLayout=view.findViewById(R.id.noDataLayout);
        searchView=view.findViewById(R.id.searchView);
        groupAPI = Constrain.createRetrofit(GroupAPI::class.java)
        participantAPI = Constrain.createRetrofit(ParticipantAPI::class.java)

         ref =Constrain.initFirebase("GroupChats")
        getAllParticipant()
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.equals("")){
                    getAllParticipant()
                }else{
                    getAllParticipantSearch(query!!)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.equals("")){
                    getAllParticipant()
                }else{
                    getAllParticipantSearch(newText!!)
                }
                return false
            }

        })
        return view
    }

    fun getAllParticipant(){
        var countData=0;
        myLoader!!.visibility=View.VISIBLE
        participantAPI!!.getAllParticipant()
            .enqueue(object : Callback<List<Participant>> {
                override fun onResponse(
                    call: Call<List<Participant>>,
                    response: Response<List<Participant>>
                ) {
                    if (response.isSuccessful) {
                        var    listP = response.body()!!
                        for (i in listP!!.indices) {
                            if(listP!![i].uid.equals(CourseTypeActivity.uid)){ // lấy ra tất cả nhóm có userid là người đang đăng nhập
                                if(listP[i].courseId.equals(CourseDetailActivity.courseId)){
                                    var idG=listP[i].groupId
                                    countData++
                                    getALGroupById(idG)

                                }

                            }

                        }
                        if(countData==0){
                            myLoader!!.visibility=View.GONE
                            noDataLayout!!.visibility=View.VISIBLE
                        }else{
                            noDataLayout!!.visibility=View.GONE

                        }

                    }
                }
                override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })


    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun getALGroupById(idG: String) {
        groupAPI!!.getGroupById(idG)
            .enqueue(object : Callback<List<Group>> {
                override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {

                    if(response.isSuccessful){
                       listG!!.addAll(response.body()!!)
                   }
                    groupChatListAdapter =   GroupChatListAdapter(context!!, listG!!,ref!!)
                    recyclerViewChatGroup!!.adapter = groupChatListAdapter
                    groupChatListAdapter!!.notifyDataSetChanged()
                    recyclerViewChatGroup!!.visibility=View.VISIBLE
                    myLoader!!.visibility=View.GONE
                }
                override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                }
            })

    }


    fun getAllParticipantSearch(keysearch:String){
        listGSearch!!.clear()
        myLoader!!.visibility=View.VISIBLE
        participantAPI!!.getAllParticipant()
            .enqueue(object : Callback<List<Participant>> {
                override fun onResponse(
                    call: Call<List<Participant>>,
                    response: Response<List<Participant>>
                ) {
                    if (response.code() == 200) {
                        var    listP = response.body()!!
                        for (i in listP!!.indices) {
                            if(listP!![i].uid.equals(CourseTypeActivity.uid)){ // lấy ra tất cả nhóm có userid là người đang đăng nhập
                                if(listP[i].courseId.equals(CourseDetailActivity.courseId)){
                                    var idG=listP[i].groupId
                                    getALGroupByIdSearch(idG,keysearch)
                                }

                            }

                        }

                    }


                }
                override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })


    }
    private fun getALGroupByIdSearch(idG: String,keysearch: String) {
        groupAPI!!.getGroupById(idG)
            .enqueue(object : Callback<List<Group>> {
                override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                   if(response.isSuccessful){
                       listG!!.addAll(response.body()!!)
                       for (i in listG!!.indices){
                           if (listG!![i].groupName!!.contains(keysearch)){
                               listGSearch!!.add(listG!![i])
                               break
                           }
                       }
                       groupChatListAdapter =   GroupChatListAdapter(context!!, listGSearch!!,ref!!)
                       recyclerViewChatGroup!!.adapter = groupChatListAdapter
                       groupChatListAdapter!!.notifyDataSetChanged()
                       recyclerViewChatGroup!!.visibility=View.VISIBLE
                       myLoader!!.visibility=View.GONE
                   }
                }

                override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                }
            })

    }




}