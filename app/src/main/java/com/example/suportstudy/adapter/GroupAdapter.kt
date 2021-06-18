package com.example.suportstudy.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.chat.ChatGroupActivity
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
        var txtHuy: TextView? = null
        var txtGroupName: TextView? = null
        init {
            IVGroup = itemView.findViewById(R.id.IVGroup)
            txtGroupName = itemView.findViewById(R.id.txtGroupName)
            txtJoin = itemView.findViewById(R.id.txtJoin)
            txtHuy = itemView.findViewById(R.id.txtHuy)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var  group:Group=list[position]
        getAllParticipant(group, holder.txtJoin!!,holder.txtHuy!!)
        holder.txtGroupName!!.text=group.groupName
        if(!group.groupImage.equals("")){
            Picasso.with(context).load(group.groupImage).placeholder(R.drawable.ic_gallery_grey).into(holder.IVGroup)
        }else{
            holder.IVGroup!!.setImageResource(R.drawable.loginimage)
        }
         holder.itemView.setOnClickListener {
             if(holder.txtJoin!!.text.equals("Đã tham gia")){
                 com.example.suportstudy.until.Until.showToast(context,"Đã tham gia")
                 var intent=Intent(context,ChatGroupActivity::class.java)
                 intent.putExtra("groupId",group._id)
                 intent.putExtra("groupCreateBy",group.createBy)
                 intent.putExtra("groupName",group.groupName)
                 intent.putExtra("groupDescription",group.groupDescription)
                 intent.putExtra("groupId",group._id)
                 context.startActivity(intent)
             }
             if(holder.txtJoin!!.text.equals("Tham gia")){
                 com.example.suportstudy.until.Until.showToast(context,"Bạn chưa tham gia nhóm này")
             }

         }
        holder.txtJoin!!.setOnClickListener {
            if(holder.txtJoin!!.text.equals("Đã tham gia")){
                com.example.suportstudy.until.Until.showToast(context,"Đã tham gia")
            }
            if(holder.txtJoin!!.text.equals("Tham gia")){
                com.example.suportstudy.until.Until.showToast(context,"Tham gia")
            }

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

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getAllParticipant(group: Group,txtJoin: TextView,txtHuy:TextView){
        txtJoin.text="Tham gia"
        participantAPI!!.getAllParticipant()
            .enqueue(object : Callback<List<Participant>> {
                override fun onResponse(
                    call: Call<List<Participant>>,
                    response: Response<List<Participant>>
                ) {
                    if (response.code() == 200) {
                     var    listP = response.body()!!
                        Log.d("sizep", listP!!.size.toString())
                        for (i in listP!!.indices) {
                            Log.d("groupid",listP!![i].groupId)
                            if(listP!![i].uid.equals(ListCourseActivity.uid)){ // lấy ra tất cả nhóm có userid là người đang đăng nhập
                                if(group._id.equals(listP!![i].groupId)){
                                    txtJoin.text="Đã tham gia"
                                    txtHuy!!.visibility=View.VISIBLE
                                }

                            }
                            if(txtJoin!!.text.equals("Tham gia")){
                                txtHuy!!.visibility=View.GONE
                            }
                        }

                    }


                }
                override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })


    }
}