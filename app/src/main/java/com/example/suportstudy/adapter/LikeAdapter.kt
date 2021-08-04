package com.example.suportstudy.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.suportstudy.model.NewsFeed
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.UserAPI
import com.example.suportstudy.until.Constrain
import kotlinx.android.synthetic.main.row_user.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeAdapter (var context: Context, var list: NewsFeed, var layout: Int) : RecyclerView.Adapter<LikeAdapter.ViewHolder>() {
    var userList: ArrayList<Users> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return LikeAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userApi = Constrain.createRetrofit(UserAPI::class.java)
        userApi.getAllUsers().enqueue(object : Callback<List<Users>>{
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if (response.isSuccessful) {
                    var    listUser= response.body()!!
                    for (i in listUser.indices){
                        var id=listUser[i]._id
                        for (j in list.like!!.indices){
                            if (id == list.like!![j].userId) {
                                userList!!.add(listUser[i])
                                Log.d("son", "listUser $userList")
                            }
                        }
                    }
                }
                if (list.like != null) {
                    holder.nameTv!!.text = userList[position].name
                    Log.d("son", "listUser $userList")
                }
            }
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun getItemCount(): Int {
        return list.like!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatarIv: ImageView? = null
        var nameTv: TextView? = null


        init {
            avatarIv = itemView.avatarIv
            nameTv = itemView.nameTv
        }
    }
}