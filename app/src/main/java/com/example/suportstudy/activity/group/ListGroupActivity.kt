package com.example.suportstudy.activity.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseTypeActivity
import com.example.suportstudy.adapter.GroupAdapter
import com.example.suportstudy.extensions.gone
import com.example.suportstudy.extensions.visible

import com.example.suportstudy.model.GroupCourse

import com.example.suportstudy.service.GroupCourseAPI
import com.example.suportstudy.until.Constrain
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListGroupActivity : AppCompatActivity() {
    var context = this@ListGroupActivity
    var groupCourseAPI: GroupCourseAPI? = null
    var rcvListGroup: RecyclerView? = null;
    var noGroupLayout: LinearLayout? = null;
    var groupAdapter: GroupAdapter? = null;
    var myLoader: LazyLoader? = null
    var txtTitle: TextView? = null

    var typedisplayGroup: String? = null
    var listMyGroup: ArrayList<GroupCourse>? = null

    var searchView: SearchView? = null
    var courseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_group)
        initViewData()

        if (typedisplayGroup.equals("allgroup")) {
            courseId = intent.getStringExtra("courseId")
            txtTitle!!.text = "Nhóm trong khóa này"
            displayAllGroup()
            searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query.equals("")) {
                        displayAllGroup()
                    } else {
                        displayAllGroupSearch(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.equals("")) {
                        displayAllGroup()

                    } else {
                        displayAllGroupSearch(newText)
                    }
                    return false
                }

            })
        }
        if (typedisplayGroup.equals("groupMyJoin")) {
            txtTitle!!.text = "Nhóm bạn đã tham gia"
            getAllMyGroupParticipant()
            searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query.equals("")) {
                        listMyGroup!!.clear()
                        getAllMyGroupParticipant()

                    } else {
                        listMyGroup!!.clear()
                        getAllMyGroupParticipantSearch(query)

                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.equals("")) {
                        listMyGroup!!.clear()
                        getAllMyGroupParticipant()

                    } else {
                        listMyGroup!!.clear()

                        getAllMyGroupParticipantSearch(newText)

                    }
                    return false

                }

            })

        }
    }

    private fun getAllMyGroupParticipantSearch(query: String?) {
        var listSearch = ArrayList<GroupCourse>()
        groupCourseAPI!!.getAllGroup().enqueue(object : Callback<List<GroupCourse>> {
            override fun onResponse(
                call: Call<List<GroupCourse>>,
                response: Response<List<GroupCourse>>
            ) {
                if (response.isSuccessful) {
                    var listGroup = response.body()
                    for (i in listGroup!!.indices) {
                        var listJoin = listGroup[i].participant
                        for (j in listJoin!!.indices) {
                            if (listJoin[j].uid.equals(CourseTypeActivity.uid)) {
                                listMyGroup!!.add(listGroup[i])
                            }
                        }
                    }
                    for (i in listMyGroup!!.indices) {
                        if (listMyGroup!![i].groupName!!.toLowerCase().contains(query!!)) {
                            listSearch.add(listMyGroup!![i])
                            break
                        }
                    }
                    setAdapter(listSearch)

                }
            }

            override fun onFailure(call: Call<List<GroupCourse>>, t: Throwable) {
                Log.e("Error", t.message.toString())

            }

        })
    }

    private fun displayAllGroupSearch(query: String?) {
        var listSearch = ArrayList<GroupCourse>()
        groupCourseAPI!!.getAllGroupByCourseID(courseId!!)
            .enqueue(object : Callback<List<GroupCourse>> {
                override fun onResponse(
                    call: Call<List<GroupCourse>>,
                    response: Response<List<GroupCourse>>
                ) {
                    if (response.isSuccessful) {
                        var listGroup = response.body()
                        for (i in listGroup!!.indices) {
                            if (listGroup[i].groupName!!.toLowerCase()
                                    .contains(query!!.toLowerCase())
                            ) {
                                listSearch.add(listGroup[i])
                            }
                        }
                        setAdapter(listSearch)
                    }
                }

                override fun onFailure(call: Call<List<GroupCourse>>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                }
            })

    }

    private fun displayAllGroup() {
        groupCourseAPI!!.getAllGroupByCourseID(courseId!!)
            .enqueue(object : Callback<List<GroupCourse>> {
                override fun onResponse(
                    call: Call<List<GroupCourse>>,
                    response: Response<List<GroupCourse>>
                ) {
                    if (response.isSuccessful) {
                        var listGroup = response.body()
                        setAdapter(listGroup!!)
                    }
                }

                override fun onFailure(call: Call<List<GroupCourse>>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                }
            })
    }

    private fun getAllMyGroupParticipant() {
        groupCourseAPI!!.getAllGroup().enqueue(object : Callback<List<GroupCourse>> {
            override fun onResponse(
                call: Call<List<GroupCourse>>,
                response: Response<List<GroupCourse>>
            ) {
                if (response.isSuccessful) {
                    var listGroup = response.body()
                    for (i in listGroup!!.indices) {
                        var listJoin = listGroup[i].participant
                        for (j in listJoin!!.indices) {
                            if (listJoin[j].uid.equals(CourseTypeActivity.uid)) {
                                listMyGroup!!.add(listGroup[i])
                            }
                        }
                    }
                    setAdapter(listMyGroup!!)
                }
            }

            override fun onFailure(call: Call<List<GroupCourse>>, t: Throwable) {
                Log.e("Error", t.message.toString())

            }

        })
    }

    fun initViewData() {
        Constrain.context = context
        rcvListGroup = findViewById(R.id.rcvListGroup)
        noGroupLayout = findViewById(R.id.noDataLayout)
        txtTitle = findViewById(R.id.txtTitle)
        myLoader = findViewById(R.id.myLoader)
        groupCourseAPI = Constrain.createRetrofit(GroupCourseAPI::class.java)
        searchView = findViewById(R.id.searchView)
        listMyGroup = ArrayList()
        var intent = intent
        typedisplayGroup = intent.getStringExtra("group")
    }

    fun setAdapter(list: List<GroupCourse>) {
        groupAdapter = GroupAdapter(context, list!!, groupCourseAPI!!)
        rcvListGroup!!.adapter = groupAdapter
        groupAdapter!!.notifyDataSetChanged()
        if (list!!.size == 0) {
            noGroupLayout!!.visible()
            myLoader!!.gone()
        } else {
            noGroupLayout!!.gone()
            myLoader!!.gone()

        }
    }


}