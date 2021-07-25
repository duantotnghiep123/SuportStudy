package com.example.suportstudy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseDetailActivity
import com.example.suportstudy.adapter.DocumentAdapter
import com.example.suportstudy.adapter.NewsFeedAdapter
import com.example.suportstudy.model.Document
import com.example.suportstudy.model.NewsFeed
import kotlinx.android.synthetic.main.fragment_news_feed.*
import kotlinx.android.synthetic.main.fragment_news_feed.view.*

class NewsFeedFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var list: ArrayList<NewsFeed>? = null
    var newsFeedAdapter: NewsFeedAdapter? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =inflater.inflate(R.layout.fragment_news_feed, container, false)
        recyclerView = view.recyclerViewPost
        list = ArrayList()
        list!!.add(NewsFeed("2256734", "Vũ Trường Sơn", "Hnay toi dang rat vui vi duoc lam du an", "17:40 19/06/2021", "https://niithanoi.edu.vn/pic/News/images/tin-tuc-cong-nghe-va-lap-trinh/kotlin-vs-java.jpg", "1132234", "345345"))
        list!!.add(NewsFeed("5666776", "Nguyễn Văn Quế", "Hnay toi dang rat vui vi", "17:40 19/06/2021", "https://jobs.hybrid-technologies.vn/wp-content/uploads/2020/06/andy-sm.png", "1132234", "345345"))
        list!!.add(NewsFeed("2364523", "Trần Minh Ngọc", "Khong biet phai noi gif bay gio", "17:40 19/06/2021", "https://media.thethao.vn//uploads/2021/06/11/ket-qua-bong-da-euro-2021-hom-nay-kqbd-euro-moi-nhat_12700.jpg", "1132234", "345345"))
        list!!.add(NewsFeed("2256734", "Đồng Phương Quang", "23h00 coi Bồ Đào Nha vs Đức đá anh em ơi", "17:40 19/06/2021", "https://vnn-imgs-f.vgcloud.vn/2021/06/18/23/nhan-dinh-duc-vs-bo-dao-nha-dai-chien-luan-anh-hung.jpg", "1132234", "345345"))
        list!!.add(NewsFeed("5677567", "Nguyễn Công Hưng", "Không biết đăng gì hết", "17:40 19/06/2021", "khong biet", "1132234", "345345"))

        newsFeedAdapter = NewsFeedAdapter(requireActivity(), list!!, R.layout.row_post)
        recyclerView!!.adapter = newsFeedAdapter
        return view
    }


}