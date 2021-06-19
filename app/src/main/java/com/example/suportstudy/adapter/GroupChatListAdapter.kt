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
import com.example.suportstudy.fragment.ChatGroupFragment
import com.example.suportstudy.model.Group
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class GroupChatListAdapter(
    var context: Context,
    var list: List<Group>,
    val ref:DatabaseReference

    ) :RecyclerView.Adapter<GroupChatListAdapter.MyViewHolder>(){
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var groupChatIv: CircleImageView? = null
        var groupNameTv: TextView? = null
        var senderName: TextView? = null
        var lastMessageTv: TextView? = null
        init {
            groupChatIv = itemView.findViewById(R.id.groupChatIv)
            groupNameTv = itemView.findViewById(R.id.groupNameTv)
            senderName = itemView.findViewById(R.id.senderName)
            lastMessageTv = itemView.findViewById(R.id.lastMessageTv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_group_chatlist, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var  group: Group =list[position]
        try {
            Picasso.with(context).load(group.groupImage)
                .into(holder.groupChatIv)
        } catch (e: Exception) {
            holder.groupChatIv!!.setImageResource(R.drawable.loginimage)
        }
        holder.groupNameTv!!.text=group.groupName
        loadLastMessage(group,holder)

        holder.itemView.setOnClickListener {
            var intent= Intent(context, ChatGroupActivity::class.java)
            intent.putExtra("groupId",group._id)
            intent.putExtra("groupCreateBy",group.createBy)
            intent.putExtra("groupImage",group.groupImage)
            intent.putExtra("groupName",group.groupName)
            intent.putExtra("groupDescription",group.groupDescription)
            context.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {
        return list.size
    }

    private fun loadLastMessage(
        group: Group,
        holder: GroupChatListAdapter.MyViewHolder,
    ) {
        // get last mess from Groups
           ref.child(group._id!!).child("Message")
            .limitToLast(1) // get last item from that child
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        // get data
                        val message = "" + ds.child("message").value
                        val sender = "" + ds.child("senderName").value
                        val type = "" + ds.child("typeMessage").value
                        if (type == "text") {
                            holder.lastMessageTv!!.setText(message)
                        } else {
                            holder.lastMessageTv!!.setText("Gửi một ảnh")
                        }
                        holder.senderName!!.setText(sender)
                        // set data
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }

            )


    }



}