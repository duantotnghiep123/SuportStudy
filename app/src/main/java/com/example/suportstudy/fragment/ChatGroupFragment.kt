package com.example.suportstudy.fragment

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
import com.example.suportstudy.activity.course.CourseTypeActivity
import com.example.suportstudy.adapter.GroupChatListAdapter
import com.example.suportstudy.model.GroupCourse

import com.example.suportstudy.service.GroupCourseAPI
import com.example.suportstudy.until.Constrain
import com.google.firebase.database.DatabaseReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatGroupFragment : Fragment() {

    var ref:DatabaseReference?=null
  var searchView:SearchView?=null
  var recyclerViewChatGroup:RecyclerView?=null
  var noDataLayout:LinearLayout?=null
  var myLoader:LazyLoader?=null


   var listMyGroup:ArrayList<GroupCourse>?= ArrayList<GroupCourse>()
   lateinit var groupCourseAPI:GroupCourseAPI
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
        groupCourseAPI = Constrain.createRetrofit(GroupCourseAPI::class.java)

         ref =Constrain.initFirebase("GroupChats")
        getAllMyGroupParticipant()
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.equals("")){
                    getAllMyGroupParticipant()
                }else{
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.equals("")){
                    getAllMyGroupParticipant()

                }else{
                }
                return false
            }

        })
        return view
    }


    private fun getAllMyGroupParticipant() {
        listMyGroup= ArrayList()
        groupCourseAPI!!.getAllGroup().enqueue(object :Callback<List<GroupCourse>>{
            override fun onResponse(
                call: Call<List<GroupCourse>>,
                response: Response<List<GroupCourse>>
            ) {
                if (response.isSuccessful){
                    var listGroup=response.body()
                    for (i in listGroup!!.indices){
                        var  listJoin=listGroup[i].participant
                        for (j in listJoin!!.indices){
                            if (listJoin[j].uid.equals(CourseTypeActivity.uid)){
                                listMyGroup!!.add(listGroup[i])
                            }
                        }
                    }
                    groupChatListAdapter =   GroupChatListAdapter(context!!, listMyGroup!!,ref!!)
                    recyclerViewChatGroup!!.adapter = groupChatListAdapter
                    groupChatListAdapter!!.notifyDataSetChanged()
                    recyclerViewChatGroup!!.visibility=View.VISIBLE
                    myLoader!!.visibility=View.GONE
                }
            }
            override fun onFailure(call: Call<List<GroupCourse>>, t: Throwable) {
                Log.e("Error",t.message.toString())

            }

        })
    }





}