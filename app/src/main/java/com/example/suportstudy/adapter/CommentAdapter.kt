package com.example.suportstudy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.model.Comment
import com.example.suportstudy.model.NewsFeed
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.UserAPI
import com.example.suportstudy.until.Constrain
import kotlinx.android.synthetic.main.fragment_comment.view.*
import kotlinx.android.synthetic.main.row_comments.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentAdapter(var context: Context, var list: NewsFeed, var layout: Int, var listComment:  Array<Comment>?) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    var userList: ArrayList<Users> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return CommentAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var comment : Array<Comment>? = list.comment

        val userApi = Constrain.createRetrofit(UserAPI::class.java)
        userApi.getAllUsers().enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if (response.isSuccessful) {
                    var    listUser= response.body()!!
                    for (i in listUser.indices){
                        var id=listUser[i]._id
                        for (j in list.like!!.indices) {
                            if (id == list.like!![j].userId) {
                                userList.add(listUser[i])
                                notifyDataSetChanged()
                            }
                        }
                    }
                }
                    if (userList.isNotEmpty()) {
                        holder.nameTv!!.text = userList[position].name
                        holder.commentTv!!.text = listComment?.get(position)!!.content
                        holder.timeTv!!.text = Constrain.formatDate(listComment!![position].createdAt)
                    }
            }
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getItemCount(): Int {
        return list.comment!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatarIv: ImageView? = null
        var nameTv: TextView? = null
        var commentTv: TextView? = null
        var timeTv: TextView? = null

        init {
            avatarIv = itemView.avatarIv
            nameTv = itemView.nameTv
            commentTv = itemView.commentTv
            timeTv = itemView.timeTv
        }
    }
}