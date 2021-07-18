package com.example.suportstudy.activity.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseDetailActivity
import com.example.suportstudy.activity.course.CourseTypeActivity
import com.example.suportstudy.adapter.GroupAdapter
import com.example.suportstudy.model.Course
import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import com.example.suportstudy.service.CourseAPI
import com.example.suportstudy.service.GroupAPI
import com.example.suportstudy.service.ParticipantAPI
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
    var backIv: ImageView? = null
    lateinit var refreshLayout: SwipeRefreshLayout

    var typedisplayGroup: String? = null
    var listMyGroup: ArrayList<GroupCourse>? = null

    var searchView: SearchView? = null
    var courseId: String? = null
    var uid:String?=null
    var userSharedPreferences: SharedPreferences? = null

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



    }
    fun initViewData(){

                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.equals("")) {
                        listMyGroup!!.clear()
                        getAllMyGroupParticipant()

        searchView=findViewById(R.id.searchView)

                    }
                    return false

                }
            })
        }
        backIv!!.setOnClickListener {
            if (typedisplayGroup.equals("groupMyJoin")) {
               Constrain.nextActivity(context,ProfileActivity::class.java)
               finish()
            }
            if (typedisplayGroup.equals("allgroup")) {
                Constrain.nextActivity(context,CourseDetailActivity::class.java)
                finish()
            }
        }
    }
    fun displayAllGroup() {
        myLoader!!.visibility=View.VISIBLE

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
                            if (listJoin[j].uid.equals(uid)) {
                                listMyGroup!!.add(listGroup[i])
                            }
                            if(listGroup!!.size==0){
                                noGroupLayout!!.visibility= View.VISIBLE
                                myLoader!!.visibility=View.GONE

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
                            myLoader!!.visibility=View.GONE
                            rcvListGroup!!.visibility=View.VISIBLE

                        }

                    }
                    override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                        Log.v("Data", "Error:" + t.message.toString())
                    }
                })
        }

    }

    fun initViewData() {
        Constrain.context = context
        rcvListGroup = findViewById(R.id.rcvListGroup)
        noGroupLayout = findViewById(R.id.noDataLayout)
        txtTitle = findViewById(R.id.txtTitle)
        backIv = findViewById(R.id.backIv)
        myLoader = findViewById(R.id.myLoader)
        groupCourseAPI = Constrain.createRetrofit(GroupCourseAPI::class.java)
        searchView = findViewById(R.id.searchView)
        refreshLayout = findViewById(R.id.refreshLayout)
        listMyGroup = ArrayList()
        var intent = intent
        typedisplayGroup = intent.getStringExtra("group")

        refreshData()
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
    fun refreshData(){
        refreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                refreshData() // your code
                refreshLayout.setRefreshing(false)
            }

}