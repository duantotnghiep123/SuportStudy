package com.example.suportstudy.activity.group

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.acount.ProfileActivity
import com.example.suportstudy.activity.course.CourseDetailActivity
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
<<<<<<< HEAD
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
=======
     var context = this@ListGroupActivity
     var groupAPI: GroupAPI? = null
     var participantAPI: ParticipantAPI? = null
     var rcvListGroup:RecyclerView?=null;
     var noGroupLayout:LinearLayout?=null;
     var groupAdapter:GroupAdapter?=null;
     var myLoader:LazyLoader?=null
    var txtTitle:TextView?=null

    var listGroup: ArrayList<Group>? = ArrayList<Group>()
    var typedisplayGroup:String?=null
    var listG:ArrayList<Group>?=ArrayList<Group>() //

    var queryS=""
    var newtextS=""
    var listSearch:ArrayList<Group>?=ArrayList<Group>() //

    var searchView:SearchView?=null
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSharedPreferences = getSharedPreferences(Constrain.SHARED_REF_USER, MODE_PRIVATE)
        uid = userSharedPreferences!!.getString(Constrain.KEY_ID, "")
        setContentView(R.layout.activity_list_group)
        initViewData()

<<<<<<< HEAD
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
=======

//        if(typedisplayGroup.equals("allgroup")){
//            txtTitle!!.text="Nhóm trong khóa này"
//            displayAllGroup()
//        }
        if(typedisplayGroup.equals("groupMyJoin")){
            txtTitle!!.text="Nhóm bạn đã tham gia"
            getAllParticipant()
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.equals("")) {
                        displayAllGroup()

<<<<<<< HEAD
                    } else {
                        displayAllGroupSearch(newText)
                    }
                    return false
                }
=======
            searchView!!.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query.equals("")){
                        listG!!.clear()
                        getAllParticipant()
                    }else{
                         queryS=query!!
                        listG!!.clear()
                        getAllParticipantSearch()
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.equals("")){
                        listG!!.clear()
                        getAllParticipant()
                    }else{
                        queryS=newText!!
                        listG!!.clear()
                        getAllParticipantSearch()
                    }
                    return false
                }

            })
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6

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

<<<<<<< HEAD
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.equals("")) {
                        listMyGroup!!.clear()
                        getAllMyGroupParticipant()
=======
        Constrain.context=context
        rcvListGroup = findViewById(R.id.rcvListGroup)
        noGroupLayout = findViewById(R.id.noGroupLayou)
        txtTitle = findViewById(R.id.txtTitle)
        myLoader = findViewById(R.id.myLoader)
        groupAPI = Constrain.createRetrofit(GroupAPI::class.java)
        participantAPI = Constrain.createRetrofit(ParticipantAPI::class.java)
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6

                    } else {
                        listMyGroup!!.clear()
                        getAllMyGroupParticipantSearch(newText)

<<<<<<< HEAD
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
=======

        var intent=intent
        typedisplayGroup=intent.getStringExtra("group")




>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
    }

<<<<<<< HEAD
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
=======
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler() { coroutineContext, throwable ->
            throwable.printStackTrace()
            Constrain.showToast("Error connection")
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            rcvListGroup!!.visibility=View.INVISIBLE
            listGroup!!.clear()
            groupAPI!!.getAllGroup()
                .enqueue(object : Callback<List<Group>> {
                    override fun onResponse(
                        call: Call<List<Group>>,
                        response: Response<List<Group>>
                    ) {
                        if (response.code() == 200) {
                            var list = response.body()
                            for (i in list!!.indices) {
                                if (list[i].courseId.equals(CourseDetailActivity.courseId)) {
                                    listGroup!!.add(list[i])
                                }
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
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
<<<<<<< HEAD

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
                listMyGroup!!.clear()
                if (response.isSuccessful) {
                    var listGroup = response.body()
                    for (i in listGroup!!.indices) {
                        var listJoin = listGroup[i].participant
                        for (j in listJoin!!.indices) {
                            if (listJoin[j].uid.equals(uid)) {
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
        backIv = findViewById(R.id.backIv)
        myLoader = findViewById(R.id.myLoader)
        groupCourseAPI = Constrain.createRetrofit(GroupCourseAPI::class.java)
        searchView = findViewById(R.id.searchView)
        refreshLayout = findViewById(R.id.refreshLayout)
        listMyGroup = ArrayList()
        var intent = intent
        typedisplayGroup = intent.getStringExtra("group")

        refreshData()
=======
    fun getAllParticipant(){
        var countid=0
        myLoader!!.visibility=View.VISIBLE
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Constrain.showToast("Data error")
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {

            participantAPI!!.getAllParticipant()
                .enqueue(object : Callback<List<Participant>> {
                    override fun onResponse(
                        call: Call<List<Participant>>,
                        response: Response<List<Participant>>
                    ) {
                        if (response.isSuccessful) {
                            var    listP = response.body()!!
                            for (i in listP!!.indices) {
                                if(listP!![i].uid.equals(CourseTypeActivity.uid)){ // lấy ra tất cả nhóm có userid là người đang đăng nhập
                                    var idG=listP[i].groupId
                                    countid++
                                    getALGroupById(idG)
                                }
                            }
                            if(countid==0){
                                myLoader!!.visibility=View.GONE
                                noGroupLayout!!.visibility=View.VISIBLE
                            }
                        }
                    }
                    override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                        Log.v("Data", "Error:" + t.message.toString())
                    }
                })
        }

    }
    fun getAllParticipantSearch(){
        var countid=0
        myLoader!!.visibility=View.VISIBLE
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Constrain.showToast("Data error")
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {

            participantAPI!!.getAllParticipant()
                .enqueue(object : Callback<List<Participant>> {
                    override fun onResponse(
                        call: Call<List<Participant>>,
                        response: Response<List<Participant>>
                    ) {
                        if (response.isSuccessful) {
                            var    listP = response.body()!!
                            for (i in listP!!.indices) {
                                if(listP!![i].uid.equals(CourseTypeActivity.uid)){ // lấy ra tất cả nhóm có userid là người đang đăng nhập
                                    var idG=listP[i].groupId
                                    countid++
                                    getALGroupBySearch(idG)
                                }
                            }
                            if(countid==0){
                                myLoader!!.visibility=View.GONE
                                noGroupLayout!!.visibility=View.VISIBLE
                            }
                        }
                    }
                    override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                        Log.v("Data", "Error:" + t.message.toString())
                    }
                })
        }

    }
    private fun getALGroupById(idG: String) {
        groupAPI!!.getGroupById(idG)
            .enqueue(object : Callback<List<Group>> {
                override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                    if(response.isSuccessful){
                        listG!!.addAll(response.body()!!)
                        if (listG!!.size==0){
                            myLoader!!.visibility=View.GONE
                            noGroupLayout!!.visibility=View.VISIBLE
                        }else{
                            myLoader!!.visibility=View.GONE
                            noGroupLayout!!.visibility=View.GONE
                            groupAdapter =   GroupAdapter(context, listG!!, participantAPI!!,groupAPI!!)
                            rcvListGroup!!.adapter = groupAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                }
            })
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6
    }
    private fun getALGroupBySearch(idG: String) {
        groupAPI!!.getGroupById(idG)
            .enqueue(object : Callback<List<Group>> {
                override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                    if(response.isSuccessful){
                        listG!!.addAll(response.body()!!)
                        for (i in listG!!.indices){
                            if (queryS.contains(listG!![i].groupName!!)){
                                listSearch!!.add(listG!![i])
                                break
                            }
                        }
                    }

<<<<<<< HEAD
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
=======
                    groupAdapter =   GroupAdapter(context, listSearch!!, participantAPI!!,groupAPI!!)
                    rcvListGroup!!.adapter = groupAdapter
                }

                override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                }
            })
    }
>>>>>>> 7578cff2be5c882010e136b88df098deabe451d6

        }
    }
    fun refreshData(){
        refreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                refreshData() // your code
                refreshLayout.setRefreshing(false)
            }

            private fun refreshData() {
                finish()
                overridePendingTransition(0, 0)
                startActivity(getIntent())
                overridePendingTransition(0, 0)
            }
        })
    }

}