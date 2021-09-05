package com.example.suportstudy.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.extensions.gone
import com.example.suportstudy.extensions.visible

import com.example.suportstudy.model.NewsFeed
import com.example.suportstudy.model.NewsFeedById
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.UserAPI
import com.example.suportstudy.until.Constrain
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.like_detail_custom.view.*
import kotlinx.android.synthetic.main.row_user.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeAdapter (var context: Context, var list: NewsFeedById, var layout: Int) : RecyclerView.Adapter<LikeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return LikeAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!list.like?.get(position)?.userId?.image.equals("noImage")) {
            var pathImageUrlUser = Constrain.baseUrl + "/post/" + list.like?.get(position)?.userId?.image?.substring(27)
            Picasso.with(context).load(pathImageUrlUser).into(holder.avatarIv)
        }
        holder.nameTv!!.text = list.like?.get(position)?.userId?.name
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