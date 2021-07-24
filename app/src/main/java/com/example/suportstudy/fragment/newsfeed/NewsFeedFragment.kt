package com.example.suportstudy.fragment.newsfeed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.adapter.NewsFeedAdapter
import com.example.suportstudy.extensions.onClick
import com.example.suportstudy.extensions.push
import com.example.suportstudy.model.NewsFeed
import com.example.suportstudy.until.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_news_feed.*
import kotlinx.android.synthetic.main.fragment_news_feed.view.*


class NewsFeedFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var newsFeedAdapter: NewsFeedAdapter? =null
    var list = ArrayList<NewsFeed>()
    private val viewModel by viewModels<NewsFeedViewModel> {
        ViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initViewModel here
        viewModel.list.observe(this){
            newsFeedAdapter = NewsFeedAdapter(requireActivity(), it, R.layout.row_post)
            Log.d("son", "newwww ${it}")
            recyclerView!!.adapter = newsFeedAdapter
        }
        Log.d("son", "vaotruoc")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("son", "vaosau")
        var view =inflater.inflate(R.layout.fragment_news_feed, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllProducts()
        recyclerView = view.recyclerViewPost
        statusBtn.onClick {
            push(R.id.newsFeedFragment_to_addNewsFeedFragment)
        }
    }

    override fun onResume() {
        Log.d("son", "resume")
        super.onResume()
    }
}