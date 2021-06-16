package com.example.suportstudy.adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseDetailActivity
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import com.example.suportstudy.service.GroupAPI
import com.example.suportstudy.service.ParticipantAPI
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupAdapter(
    var context: Context,
    var list: List<Group>, var participantAPI:ParticipantAPI,var groupAPI: GroupAPI) : RecyclerView.Adapter<GroupAdapter.MyViewHolder>() {
    var listGroupMyParticipant:ArrayList<Group>?=ArrayList<Group>()
    var listP:List<Participant>?= ArrayList<Participant>()



    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var IVGroup: CircleImageView? = null
        var txtJoin: TextView? = null
        var txtGroupName: TextView? = null
        init {
            IVGroup = itemView.findViewById(R.id.IVGroup)
            txtGroupName = itemView.findViewById(R.id.txtGroupName)
            txtJoin = itemView.findViewById(R.id.txtJoin)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var  group:Group=list[position]
        holder.txtGroupName!!.text=group.groupName
        if(!group.groupImage.equals("")){
            Picasso.with(context).load(group.groupImage).placeholder(R.drawable.ic_gallery_grey).into(holder.IVGroup)
        }else{
            holder.IVGroup!!.setImageResource(R.drawable.loginimage)
        }

        holder.txtJoin!!.setOnClickListener {

            var time=System.currentTimeMillis().toString()
            Log.d("join", time+""+ListCourseActivity.uid+"__"+group._id)

//            participantAPI.insertParticipant(time,ListCourseActivity.uid, group._id!!)
//                   .enqueue(object : retrofit2.Callback<Participant> {
//                       override fun onResponse(
//                           call: retrofit2.Call<Participant>,
//                           response: Response<Participant>
//                       ) {
//                           if (response.isSuccessful) {
//                              holder.txtJoin!!.text="Đã tham gia"
//                           }
//                       }
//                       override fun onFailure(call: retrofit2.Call<Participant>, t: Throwable) {
//                           Log.v("Data", "Error: " + t.message.toString())
//                       }
//                   })

        }
        getAllParticipant(group, holder.txtJoin!!)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun test() {
        participantAPI!!.getAllParticipant()
            .enqueue(object : Callback<List<Participant>> {
                override fun onResponse(
                    call: Call<List<Participant>>,
                    response: Response<List<Participant>>
                ) {
                    if (response.code() == 200) {
                      var  listP = response.body()
                        for (i in listP!!.indices) {
                            if (listP!![i].uid.equals(ListCourseActivity.uid) && listP!![i].courseId.equals(
                                    CourseDetailActivity.courseId
                                ) ) {
                                var idG = listP!![i].groupId
                                getGroup(idG)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })
    }

    fun getGroup(idG: String) {
        Log.d("id", idG)
        groupAPI!!.getGroupById(idG)
            .enqueue(object : Callback<List<Group>> {
                override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                    listGroupMyParticipant!!.addAll(response.body()!!)
                    Log.d("sizetestAdapter",listGroupMyParticipant!!.size.toString())
                }
                override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                }
            })

    }
    fun getAllParticipant(group: Group,textView: TextView){
        participantAPI!!.getAllParticipant()
            .enqueue(object : Callback<List<Participant>> {
                override fun onResponse(
                    call: Call<List<Participant>>,
                    response: Response<List<Participant>>
                ) {
                    if (response.code() == 200) {
                     var    list2 = response.body()!!
                        Log.d("sizep", list2!!.size.toString())
                        var b=0;
                        for (i in list2!!.indices) {
                            Log.d("groupid",list2!![i].groupId)
                            if(list2!![i].uid.equals(ListCourseActivity.uid)){
                                if(group._id.equals(list2!![i].groupId)){
                                    textView.text="Đã tham gia"
                                }
                            }
                            Log.d("count", b.toString())

//                            if (listP!![i].uid.equals(ListCourseActivity.uid) && group._id.equals(listP!![i].groupId) ) {
//                                txtJoin.text="Đã tham gia"
//                            }else{
//                                txtJoin.text="Tham gia"
//
//                            }
                        }
                    }
                }
                override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })


    }
}