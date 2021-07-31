package com.example.suportstudy.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.extensions.*
import com.example.suportstudy.fragment.comment.CommentFragment
import com.example.suportstudy.model.*
import com.example.suportstudy.service.NewsFeedAPI
import com.example.suportstudy.until.Constrain
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_post.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NewsFeedAdapter(var context: Context, var list: ArrayList<NewsFeed>, var layout: Int, var userLocal: Users ) :
    RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>() {
    var sd: SweetAlertDialog? = null

    var sharedPreferences: SharedPreferences? = null
    var checkLike = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelPost: NewsFeed = list!![position]
//        val uid: String = modelPost.userId._id
        val id: String = modelPost._id
        val description: String = modelPost.description
        val coursesId: String = modelPost.typeClassId
        val time: String = modelPost.createdAt
        val user: Users = modelPost.userId
        val type: String = modelPost.image
        var comment: Int = modelPost.comment!!.size
        var like = modelPost.like!!.size
        if (layout == R.layout.row_post) {
            sd = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            sd!!.titleText = "Loading"
            sd!!.setCancelable(true)
            if (user != null){
                holder.uNameTv.text = user.name
            }

            holder.pTimeTv.text = modelPost.createdAt
            holder.pDescriptionTv.text = modelPost.description
            holder.pLikeTv.text = like.toString()
            holder.pCommentTv.text = comment.toString() + " Comment"
            var pathImageUrl=""
            pathImageUrl = Constrain.baseUrl + "/post/" + modelPost.image.substring(26)
            Picasso.with(context).load(pathImageUrl).into(holder.pImageIv)

            fun getLikeByUserId(userId: String): Boolean {
                if (modelPost.like.isEmpty()) {
                    return false
                }
                modelPost.like.forEach {
                    if (it.userId?.equals(userId)) {
                        return true
                    }
                }
                return false
            }
            if (getLikeByUserId(userLocal._id)) {
                holder.likeIv.setImageResource(R.drawable.ic_liked)
            }

            holder.likeIv.onClick {
                holder.likeIv.disable()
                checkLike = false
                if (checkLike) {

                }else{
                    like++
                    holder.likeIv.setImageResource(R.drawable.ic_liked)
                    holder.pLikeTv.text = like.toString()
                    val newsFeedApi = Constrain.createRetrofit(NewsFeedAPI::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        newsFeedApi.addLike(true, userLocal._id, modelPost._id).enqueue(object : Callback<AddLike> {
                            override fun onResponse(call: Call<AddLike>, response: Response<AddLike>) {
//                                holder.likeIv.enable()
                                Log.d("son", "like thanh cong")
                            }
                            override fun onFailure(call: Call<AddLike>, t: Throwable) {
//                                holder.likeIv.enable()
//                                like--
//                                holder.likeIv.setImageResource(R.drawable.ic_like)
//                                holder.pLikeTv.text = like.toString()
//                                Log.d("son", "like that bai")
                            }
                        })
                    }
                }
            }
            holder.commentBtn.onClick {
                val fragment: Fragment = CommentFragment()
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var uPictureIv: ImageView
        var pImageIv: ImageView
        var likeIv: ImageView
        var uNameTv: TextView
        var pTimeTv: TextView
        var pDescriptionTv: TextView
        var pLikeTv: TextView
        var pCommentTv: TextView
        var moreBtn: ImageButton
        var likeBtn: LinearLayout
        var commentBtn: LinearLayout
        var seenByLikeBtn: LinearLayout
        var profileLayout: RelativeLayout

        init {
            uPictureIv = itemView.uPictureIv
            likeIv = itemView.likeIv
            pImageIv = itemView.pImageIvPost
            pCommentTv = itemView.pCommentTv
            uNameTv = itemView.uNameTv
            pTimeTv = itemView.pTimeTv
            pDescriptionTv = itemView.pDescriptionTv
            pLikeTv = itemView.pLikeTv
            moreBtn = itemView.moreBtn
            likeBtn = itemView.likeBtn
            commentBtn = itemView.commentBtn
            seenByLikeBtn = itemView.shareBtn
            profileLayout = itemView.reaHeader
        }
    }
}