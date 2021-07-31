package com.example.suportstudy.fragment.newsfeed

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.adapter.NewsFeedAdapter
import com.example.suportstudy.extensions.gone
import com.example.suportstudy.extensions.onClick
import com.example.suportstudy.extensions.push
import com.example.suportstudy.model.NewsFeed
import com.example.suportstudy.model.Users
import com.example.suportstudy.until.Constrain
import com.example.suportstudy.until.ViewModelFactory
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.fragment_news_feed.*
import kotlinx.android.synthetic.main.fragment_news_feed.view.*


class NewsFeedFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var newsFeedAdapter: NewsFeedAdapter? =null
    var list = ArrayList<NewsFeed>()
    var sharedPreferences: SharedPreferences? = null
    private val viewModel by viewModels<NewsFeedViewModel> {
        ViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initViewModel here
        viewModel.getAllProducts()
        sharedPreferences = requireContext().getSharedPreferences(
            Constrain.SHARED_REF_USER,
            Context.MODE_PRIVATE
        )
        val _id = sharedPreferences?.getString(Constrain.KEY_ID, "")
        val name = sharedPreferences?.getString(Constrain.KEY_NAME, "")
        val image = sharedPreferences?.getString(Constrain.KEY_IMAGE, "")
        val email = sharedPreferences?.getString(Constrain.KEY_EMAIL, "")
        val isTurtor = sharedPreferences?.getBoolean(Constrain.KEY_ISTUTOR, true)
        var user = Users(_id!!, name!!, image!!, email!!,"" ,isTurtor!!)
        viewModel.list.observe(this){
            newsFeedAdapter = NewsFeedAdapter(requireActivity(), it, R.layout.row_post, user)
            recyclerView!!.adapter = newsFeedAdapter
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =inflater.inflate(R.layout.fragment_news_feed, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.recyclerViewPost
        if (sharedPreferences?.getBoolean(Constrain.KEY_ISTUTOR, true) == false) {
            statusBtn.gone()
        }
        statusBtn.onClick {
            push(R.id.newsFeedFragment_to_addNewsFeedFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProducts()
    }
}