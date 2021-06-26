package com.example.suportstudy.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.activity.chat.ChatGroupActivity
import com.example.suportstudy.activity.course.CourseTypeActivity
import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import com.example.suportstudy.service.GroupAPI
import com.example.suportstudy.service.ParticipantAPI
import com.example.suportstudy.until.Constrain
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupAdapter(
    var context: Context,
    var list: List<Group>, var participantAPI:ParticipantAPI,var groupAPI: GroupAPI) : RecyclerView.Adapter<GroupAdapter.MyViewHolder>() {


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
        getAllParticipant(group, holder.txtJoin!!)
        var groupImage= group.groupImage!!
        holder.txtGroupName!!.text=group.groupName
        var pathImageUrl=""
        if(groupImage.equals("noImage")){
           pathImageUrl=""
        }else{
            pathImageUrl = Constrain.baseUrl + "/group/" + group.groupImage!!.substring(27)
        }

        Constrain.checkShowImage(context,pathImageUrl, holder.IVGroup!!)



         holder.itemView.setOnClickListener {
             if(holder.txtJoin!!.text.equals("Đã tham gia")){
                 var intent=Intent(context,ChatGroupActivity::class.java)
                 intent.putExtra("groupId",group._id)
                 intent.putExtra("groupCreateBy",group.createBy)
                 intent.putExtra("groupImage",group.groupImage)
                 intent.putExtra("groupName",group.groupName)
                 intent.putExtra("groupDescription",group.groupDescription)
                 context.startActivity(intent)
             }
              else if(holder.txtJoin!!.text.equals("Tham gia")){
                 Constrain.showToast(context,"Bạn chưa tham gia nhóm này")
             }

         }
        holder.txtJoin!!.setOnClickListener {

            if(holder.txtJoin!!.text.equals("Tham gia")){
                var time=System.currentTimeMillis().toString()

            participantAPI.insertParticipant(time,CourseTypeActivity.uid, group._id!!,group.courseId!!)
                   .enqueue(object : Callback<Participant> {
                       override fun onResponse(
                           call:Call<Participant>,
                           response: Response<Participant>
                       ) {
                           if (response.isSuccessful) {
                               holder.txtJoin!!.text="Đã tham gia"
                           }
                       }
                       override fun onFailure(call: Call<Participant>, t: Throwable) {
                           Log.v("Data", "Error: " + t.message.toString())
                       }
                   })
            }



        }

    }
    override fun getItemCount(): Int {
        return list.size
    }
    fun getAllParticipant(group: Group,txtJoin: TextView){

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
                            if(listP!![i].uid.equals(CourseTypeActivity.uid)){ // lấy ra tất cả nhóm có userid là người đang đăng nhập
                                if(group._id.equals(listP!![i].groupId)){
                                    txtJoin.text="Đã tham gia"
                                }
                            }
                            if(!listP!![i].uid.equals(CourseTypeActivity.uid)){ // lấy ra tất cả nhóm có userid là người đang đăng nhập
                                if(group._id.equals(listP!![i].groupId)){
                                    txtJoin.text="Tham gia"
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
}