package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseDetailActivity
import com.example.suportstudy.adapter.DocumentAdapter
import com.example.suportstudy.model.Document
import com.example.suportstudy.until.Constrain
import java.util.*
import kotlin.collections.ArrayList


class DocumentFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var list: ArrayList<Document>? = null
    var documentAdapter: DocumentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view=inflater.inflate(R.layout.fragment_document, container, false)


        recyclerView = view.findViewById<RecyclerView>(R.id.rcvDocument)
        list = ArrayList()
        for (i in 1..23) {
            list!!.add(Document("Bài $i", CourseDetailActivity.imageUrl))
        }
        documentAdapter = DocumentAdapter(activity!!, list!!)
        recyclerView!!.adapter = documentAdapter
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DocumentFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}