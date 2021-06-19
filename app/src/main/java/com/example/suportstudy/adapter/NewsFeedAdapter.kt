package com.example.suportstudy.adapter

import android.accounts.AccountManager.get
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.model.Document
import com.example.suportstudy.model.NewsFeed
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_post.view.*
import okhttp3.HttpUrl.get
import java.util.*

class NewsFeedAdapter(var context: Context, var list: ArrayList<NewsFeed>,  var layout: Int) :
    RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>() {
    var sd: SweetAlertDialog? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelPost: NewsFeed = list!![position]
        val uid: String = modelPost.uId
        val id: String = modelPost.id
        val description: String = modelPost.description
        val coursesId: String = modelPost.coursesId
        val time: String = modelPost.time
        val title: String = modelPost.title
        val type: String = modelPost.type
        if (layout == R.layout.row_post) {
            sd = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            sd!!.titleText = "Loading"
            sd!!.setCancelable(true)
//            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = time.toLong()
//            val pTime = DateFormat.format("dd/MM/yyyy  hh:mm aa", calendar).toString()
            holder.uNameTv.text = modelPost.title
            holder.pTimeTv.text = modelPost.time
            holder.pDescriptionTv.text = modelPost.description
            Picasso.with(context).load(modelPost.type).into(holder.pImageIv)
//            holder.pImageIv.setImageURI(Uri.parse("https://niithanoi.edu.vn/pic/News/images/tin-tuc-cong-nghe-va-lap-trinh/kotlin-vs-java.jpg"))

//            setLiked(modelPost.getpId(), holder.likeIv);
//            setLikes(holder, id)
//            setTextCoutLike(holder.pLikeTv, modelPost.getpId())
//            setCommentCount(modelPost.getpId(), holder.pCommentTv)
//            try {
//                if (uAvatar != "") {
//                    Picasso.with(context).load(uAvatar).placeholder(R.drawable.avatar_default)
//                        .into(holder.uPictureIv)
//                } else {
//                    holder.uPictureIv.setImageResource(R.drawable.avatar_default)
//                }
//            } catch (e: Exception) {
//                holder.uPictureIv.setImageResource(R.drawable.avatar_default)
//            }
//            // nếu status không hình thì sẽ ấn imageview , ngược lại hiện
//            if (pImage == "noImage") {
//                holder.pImageIv.visibility = View.GONE
//            } else {
//                try {
//
//                    Picasso.with(context).load(pImage).placeholder(R.drawable.ic_gallery_grey)
//                        .into(holder.pImageIv)
//                } catch (e: Exception) {
//                    holder.pImageIv.setImageResource(R.drawable.ic_gallery_grey)
//                }
//            }
//            holder.commentBtn.setOnClickListener {
//                val intent = Intent(context, PostDetailActivity::class.java)
//                intent.putExtra(
//                    "postId",
//                    pId
//                ) // truyền bài đăng có id này qua PostDetailactivity để show dữu liệu của bài viết này
//                intent.putExtra(
//                    "count",
//                    com.example.italkapp.adapter.AdapterPost.count
//                ) // truyền bài đăng có id này qua PostDetailactivity để show dữu liệu của bài viết này
//                context!!.startActivity(intent)
//            }
//            holder.seenByLikeBtn.setOnClickListener {
//                val intent = Intent(context, PostLikeByActivity::class.java)
//                intent.putExtra("postId", pId)
//                context!!.startActivity(intent)
//            }
//            holder.likeBtn.setOnClickListener {
//                checkLike = true
//                val postId: String = postsList!![position].getpId()
//                postsRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        if (checkLike) {
//                            if (dataSnapshot.child(postId).child("Likes").hasChild(myUid)) {
//                                postsRef.child(postId).child("Likes").child(myUid).removeValue()
//                                checkLike = false
//                            } else {
//                                postsRef.child(postId).child("Likes").child(myUid).setValue(true)
//                                com.example.italkapp.adapter.AdapterPost.count++
//                                addToNotifications(uid, pId, "like")
//                                checkLike = false
//                            }
//                        }
//                    }
//
//                    override fun onCancelled(databaseError: DatabaseError) {}
//                })
//            }
//            // chuyển màn hình qua profile đã đăng post đã click
//            holder.profileLayout.setOnClickListener { v ->
//                // nếu là mã id trong bài đăng là của user đang đăng nhập thì chuyển qua profile chính user đó ngược lại chuyển qua progile user khác
//                if (myUid == uid) {
//                    val activity = v.context as AppCompatActivity
//                    val profileFragment = ProfileFragment()
//                    val bundle = Bundle()
//                    //                    bundle.putString("linktrang2", postsList.get(position).getpId());
//                    profileFragment.setArguments(bundle)
//                    activity.supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.frame_container, profileFragment)
//                        .addToBackStack(null)
//                        .commit()
//                } else {
//                    val intent = Intent(context, HisProfileActivity::class.java)
//                    intent.putExtra("hisUid", uid)
//                    context!!.startActivity(intent)
//                }
//            }
//            holder.moreBtn.setOnClickListener {
//                showMoreOption(
//                    holder.moreBtn,
//                    uid,
//                    myUid,
//                    pId,
//                    pDescription,
//                    pImage
//                )
//            }
//            holder.pImageIv.setOnLongClickListener {
//                showOptionImage(modelPost.getpImage())
//                //                    showDialogDownLoadImage(modelPost.getpImage());
//                false
//            }
//        }
//        if (layout == R.layout.row_post_recent) {
//            modelPost.getuName().trim()
//            val name: Array<String> = modelPost.getuName().split("\\s+")
//            holder.uNameTv.text = name[name.size - 1]
//            try {
//                Picasso.with(context).load(pImage).placeholder(R.drawable.ic_gallery_grey)
//                    .into(holder.pImageIv)
//            } catch (e: Exception) {
//                holder.pImageIv.setImageResource(R.drawable.ic_gallery_grey)
//            }
//            holder.itemView.setOnClickListener {
//                val intent = Intent(context, PostDetailActivity::class.java)
//                intent.putExtra(
//                    "postId",
//                    pId
//                ) // truyền bài đăng có id này qua PostDetailactivity để show dữu liệu của bài viết này
//                intent.putExtra(
//                    "count",
//                    com.example.italkapp.adapter.AdapterPost.count
//                ) // truyền bài đăng có id này qua PostDetailactivity để show dữu liệu của bài viết này
//                context!!.startActivity(intent)
//            }
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