package com.example.suportstudy.adapter

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.model.Chat
import com.example.suportstudy.until.Until
import com.squareup.picasso.Picasso
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import java.util.*

class ChatAdapter(var context: Context, var chatList: List<Chat>) :RecyclerView.Adapter<ChatAdapter.MyViewHolder>(){
    private val MSG_TYPE_LEFT = 0
    private val MSG_TYPE_RIGHT = 1




    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var profileIv: ImageView? = null
        var messageIv:ImageView? = null
        var messageTv: TextView? = null
        var timeTv:TextView? = null
        var isSeenTv:TextView? = null
        var messageLayout: LinearLayout? = null

        init {
            profileIv = itemView.findViewById(R.id.profileIv)
            messageTv = itemView.findViewById(R.id.messageTv)
            timeTv = itemView.findViewById(R.id.timeTv)
            isSeenTv = itemView.findViewById(R.id.isSeendTv)
            messageLayout = itemView.findViewById(R.id.messageLayout)
            messageIv = itemView.findViewById(R.id.messageIv)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        if (viewType == MSG_TYPE_RIGHT) {
            val view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false)
            return MyViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false)
            return MyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message: String = chatList.get(position).message!!
        val timeStamp: String = chatList.get(position).timeSend!!
        val type: String = chatList.get(position).messageType!!
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timeStamp.toLong()
        val dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString()

        // gán dữ liệu
        if (type == "text") {
            holder.messageTv!!.visibility = View.VISIBLE
            holder.messageIv!!.visibility = View.GONE
            holder.messageTv!!.text = message
        } else {
            holder.messageTv!!.visibility = View.GONE
            holder.messageIv!!.visibility = View.VISIBLE
            Picasso.with(context).load(message).placeholder(R.drawable.ic_gallery_grey)
                .into(holder.messageIv)
        }
        // gán dữ liệu
        holder.messageTv!!.text = message
        holder.timeTv!!.text = dateTime
    }

    override fun getItemViewType(position: Int): Int {

        return if (chatList[position].senderUid.equals("60c5527bd45d2165ec51fd6f")) {
            return MSG_TYPE_RIGHT
        } else {
            return MSG_TYPE_LEFT
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }


}