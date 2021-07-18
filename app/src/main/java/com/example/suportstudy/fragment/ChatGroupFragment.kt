package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity
=======
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.ActionActivity
import com.example.suportstudy.activity.course.CourseTypeActivity
import com.example.suportstudy.adapter.GroupChatListAdapter
import com.example.suportstudy.extensions.gone
import com.example.suportstudy.extensions.visible
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

   var listMyGroup:ArrayList<GroupCourse>?= null
   var listMyGroupSearch:ArrayList<GroupCourse>?= null
   lateinit var groupCourseAPI:GroupCourseAPI
    var uid: String? = null
    var userSharedPreferences: SharedPreferences? = null

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
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_chat_group, container, false)
        userSharedPreferences = activity!!.getSharedPreferences(Constrain.SHARED_REF_USER,
            AppCompatActivity.MODE_PRIVATE
        )
        uid = userSharedPreferences!!.getString(Constrain.KEY_ID, "")
        recyclerViewChatGroup=view.findViewById(R.id.recyclerViewChatGroup);
        myLoader=view.findViewById(R.id.myLoader);
        noDataLayout=view.findViewById(R.id.noDataLayout);
        searchView=view.findViewById(R.id.searchView);
        groupCourseAPI = Constrain.createRetrofit(GroupCourseAPI::class.java)
        listMyGroup= ArrayList()
        listMyGroupSearch= ArrayList()
         ref =Constrain.initFirebase("GroupChats")
        getAllMyGroupParticipant()

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.equals("")){
                    listMyGroup!!.clear()
                    getAllMyGroupParticipant()
                }else{
                    listMyGroup!!.clear()
                    getAllMyGroupParticipantSearch(query!!)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.equals("")){
                    listMyGroup!!.clear()
                    getAllMyGroupParticipant()

                }else{
                    listMyGroup!!.clear()
                    getAllMyGroupParticipantSearch(newText!!)

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
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6

    private fun getAllMyGroupParticipant() {

        groupCourseAPI!!.getAllGroup().enqueue(object :Callback<List<GroupCourse>>{
            override fun onResponse(
                call: Call<List<GroupCourse>>,
                response: Response<List<GroupCourse>>
            ) {
                listMyGroup!!.clear()
                if (response.isSuccessful){
                    var listGroup=response.body()
                    for (i in listGroup!!.indices){
                        var  listJoin=listGroup[i].participant
                        for (j in listJoin!!.indices){
                            if (listJoin[j].uid.equals(uid)){
                                listMyGroup!!.add(listGroup[i])
                            }
                        }
<<<<<<< HEAD
=======
                        if(countData==0){
                            myLoader!!.visibility=View.GONE
                            noDataLayout!!.visibility=View.VISIBLE
                        }else{
                            noDataLayout!!.visibility=View.GONE

                        }

>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
                    }
                     setAdapter(listMyGroup!!)
                }
<<<<<<< HEAD
            }
            override fun onFailure(call: Call<List<GroupCourse>>, t: Throwable) {
                Log.e("Error",t.message.toString())

            }
=======
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
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6

        })
    }
    private fun getAllMyGroupParticipantSearch(query:String) {
        groupCourseAPI!!.getAllGroup().enqueue(object :Callback<List<GroupCourse>>{
            override fun onResponse(
                call: Call<List<GroupCourse>>,
                response: Response<List<GroupCourse>>
            ) {
                listMyGroup!!.clear()
                listMyGroupSearch!!.clear()

                if (response.isSuccessful){
                    var listGroup=response.body()
                    for (i in listGroup!!.indices){
                        var  listJoin=listGroup[i].participant
                        for (j in listJoin!!.indices){
                            if (listJoin[j].uid.equals(uid)){
                                listMyGroup!!.add(listGroup[i])
                            }
                        }
                    }
                    for (i in listMyGroup!!.indices){
                        if (listMyGroup!![i].groupName!!.toLowerCase().contains(query.toLowerCase())) {
                            listMyGroupSearch!!.add(listMyGroup!![i])
                        }
                    }
                     setAdapter(listMyGroupSearch!!)
                }
            }
            override fun onFailure(call: Call<List<GroupCourse>>, t: Throwable) {
                Log.e("Error",t.message.toString())

            }

        })
    }


fun setAdapter( listGroup: ArrayList<GroupCourse>){
    groupChatListAdapter =   GroupChatListAdapter(requireContext(), listGroup,ref!!)
    recyclerViewChatGroup!!.adapter = groupChatListAdapter
    groupChatListAdapter!!.notifyDataSetChanged()
    if(listGroup.size==0){
        noDataLayout!!.visible()
        recyclerViewChatGroup!!.gone()
        myLoader!!.gone()
    }else{
        noDataLayout!!.gone()
        recyclerViewChatGroup!!.visible()
        myLoader!!.gone()
    }

}

    override fun onStart() {
        super.onStart()
        getAllMyGroupParticipant()
    }


}