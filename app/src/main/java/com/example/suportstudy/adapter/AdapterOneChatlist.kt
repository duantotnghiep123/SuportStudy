package com.example.suportstudy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.model.Users
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdapterOneChatlist(
    var context: Context,
    var list: List<Users>,

) :RecyclerView.Adapter<AdapterOneChatlist.MyViewHolder>(){
    private val lastMessageMap: HashMap<String, String>? = HashMap()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var chatIv: CircleImageView? = null
        var nameTv: TextView? = null
        var senderName: TextView? = null
        var lastMessageTv: TextView? = null
        init {
            chatIv = itemView.findViewById(R.id.groupChatIv)
            nameTv = itemView.findViewById(R.id.groupNameTv)
            senderName = itemView.findViewById(R.id.senderName)
            lastMessageTv = itemView.findViewById(R.id.lastMessageTv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_group_chatlist, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var user=list[position]

        val userImage: String = user.image
        val userName: String = user.name
        val hisUid: String = user._id
        holder.senderName!!.visibility=View.GONE

        val lastMessage = lastMessageMap!![hisUid]

        holder.nameTv!!.text = userName
        if (lastMessage == null || lastMessage == "default") {
            holder.lastMessageTv!!.visibility = View.GONE
        } else {
            holder.lastMessageTv!!.visibility = View.VISIBLE
            holder.lastMessageTv!!.text = lastMessage
        }
        try {
            Picasso.with(context).load(userImage).placeholder(R.drawable.avatar_default)
                .into(holder.chatIv)
        } catch (e: Exception) {
           holder.chatIv!!.setImageResource(R.drawable.avatar_default)
        }
    }
    fun setLastMessageMap(userId: String?, lastMessage: String?) {
        lastMessageMap!![userId!!] = lastMessage!!
    }
    override fun getItemCount(): Int {
       return list.size
    }
}