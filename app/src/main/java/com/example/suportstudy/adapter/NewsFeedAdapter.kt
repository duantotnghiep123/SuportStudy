package com.example.suportstudy.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.extensions.*
import com.example.suportstudy.fragment.newsfeed.NewsFeedViewModel
import com.example.suportstudy.model.*
import com.example.suportstudy.service.NewsFeedAPI
import com.example.suportstudy.until.Constrain
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_comment.view.*
import kotlinx.android.synthetic.main.row_post.view.*
import kotlinx.android.synthetic.main.row_post.view.likeBtn
import kotlinx.android.synthetic.main.row_post.view.likeIv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class NewsFeedAdapter(var context: Context, var list: ArrayList<NewsFeed>, var layout: Int, var userLocal: Users ) :
    RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>() {
    var sd: SweetAlertDialog? = null
    var commentAdapter: CommentAdapter? = null
    var likeAdapter: LikeAdapter? = null
    var sharedPreferences: SharedPreferences? = null
    var checkLike = false
    val viewModel : NewsFeedViewModel? = null


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
        var pathImageUrl=""
        pathImageUrl = Constrain.baseUrl + "/post/" + modelPost.image.substring(26)
        if (layout == R.layout.row_post) {
            sd = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            sd!!.titleText = "Loading"
            sd!!.setCancelable(true)
            if (user != null){
                holder.uNameTv.text = user.name
            }

            Picasso.with(context).load(pathImageUrl).into(holder.uPictureIv)
            holder.pTimeTv.text = Constrain.formatDate(modelPost.createdAt)
            holder.pDescriptionTv.text = modelPost.description
            holder.pLikeTv.text = like.toString() + " Thích"
            holder.pCommentTv.text = comment.toString() + " Bình luận"
            Picasso.with(context).load(pathImageUrl).into(holder.pImageIv)



            if (getLikeByUserId(userLocal._id, modelPost)) {
                holder.likeIv.setImageResource(R.drawable.ic_liked)
                holder.likeIv.disable()
            }
            else {
                holder.likeIv.setImageResource(R.drawable.ic_like)
            }

            holder.likeIv.onClick {
                holder.likeIv.disable()
                checkLike = false
                if (checkLike) {

                }else{
                    like++
                    holder.likeIv.setImageResource(R.drawable.ic_liked)
                    holder.pLikeTv.text = like.toString() + " Thích"
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
                                Log.d("son", "like that bai")
                            }
                        })
                    }
                }
            }
            holder.commentBtn.onClick {
                showDialogComment(modelPost, userLocal, position)
            }
            holder.seeLikeBtn.onClick {
                showDialogLike(modelPost)
                Log.d("son", "userIdLike ${modelPost.like}")
            }
        }
    }
    private fun getLikeByUserId(userId: String, newsFeed: NewsFeed): Boolean {
        if (newsFeed.like!!.isEmpty()) {
            return false
        }
        newsFeed.like.forEach {
            if (it.userId == userId) {
                return true
            }
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogComment(newsFeed: NewsFeed, userLocal: Users, position: Int) {
        val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar)
        dialog.window?.setWindowAnimations(R.style.Theme_Dialog_Animation)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_comment)
        val btnback = dialog.findViewById(R.id.backIv) as ImageView
        val edtComment = dialog.findViewById(R.id.commentEt) as EditText
        val btnSend = dialog.findViewById(R.id.senBtn) as ImageView
        val tvLike = dialog.findViewById(R.id.likeCommentTv) as TextView
        var loader = dialog.findViewById(R.id.myLoader) as LazyLoader
        val listComment: Array<Comment>? = list[position].comment
        commentAdapter = CommentAdapter(context, list[position], R.layout.row_comments, list[position].comment)
        val listRcv: RecyclerView = dialog.findViewById(R.id.recyclerView)
        val hideCommentLayout : LinearLayout = dialog.findViewById(R.id.hideCommentLayout)
        listRcv.layoutManager = LinearLayoutManager(context)
        listRcv.adapter = commentAdapter

        tvLike.text =  newsFeed.like!!.size.toString() + " Thích"
        if (newsFeed.comment!!.isEmpty()) {
            hideCommentLayout.visible()
        }
        btnback.onClick { dialog.dismiss() }
        btnSend.onClick {
            if (TextUtils.isEmpty(edtComment.text.toString())) {
                Constrain.showToast("Vui lòng không để trống")
            }
            else {
                disableSoftInputFromAppearing(edtComment)
                loader.visible()
                btnSend.disable()
                val newsFeedApi = Constrain.createRetrofit(NewsFeedAPI::class.java)
                newsFeedApi.addComment(edtComment.text.toString(), userLocal._id, newsFeed._id).enqueue(object  : Callback<Comment>{
                    override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                        Log.d("son", "comment thanh cong")
                        loader.gone()
                        btnSend.enable()
                        edtComment.text.clear()
                        dialog.dismiss()

//                        listComment.add(Comment("","","",""))
                        commentAdapter!!.notifyDataSetChanged()
                    }
                    override fun onFailure(call: Call<Comment>, t: Throwable) {
                        loader.gone()
                        btnSend.enable()
                        Log.d("son", "comment that bai")
                    }
                })
            }
        }
        dialog.show()
    }

    private fun showDialogLike(newsFeed: NewsFeed) {
        val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar)
        dialog.window?.setWindowAnimations(R.style.Theme_Dialog_Animation)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.like_detail_custom)
        val btnback = dialog.findViewById(R.id.backIv) as ImageView
        likeAdapter = LikeAdapter(context, newsFeed, R.layout.row_user)
        val list: RecyclerView = dialog.findViewById(R.id.recyclerviewLike)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = likeAdapter
        btnback.onClick { dialog.dismiss() }
        dialog.show()
    }

    private fun disableSoftInputFromAppearing(editText: EditText) {
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        editText.setTextIsSelectable(true)
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
        var seeLikeBtn: LinearLayout
        var profileLayout: RelativeLayout
        var recyclerView: RecyclerView? = null

        init {
            uPictureIv = itemView.uPictureIv
            likeIv = itemView.likeIv
            pImageIv = itemView.pImageIvPost
            pCommentTv = itemView.commentTv
            uNameTv = itemView.uNameTv
            pTimeTv = itemView.pTimeTv
            pDescriptionTv = itemView.pDescriptionTv
            pLikeTv = itemView.likeTv
            moreBtn = itemView.moreBtn
            likeBtn = itemView.likeBtn
            seeLikeBtn = itemView.seeLikeBtn
            commentBtn = itemView.commentBtn
            profileLayout = itemView.reaHeader
            recyclerView = itemView.recyclerView
        }
    }
}