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

    var querySearch=""
    var listSearch:ArrayList<Group>?=ArrayList<Group>() //

    var searchView:SearchView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_group)
        initViewData()


        if(typedisplayGroup.equals("allgroup")){
            txtTitle!!.text="Nhóm trong khóa này"
            displayAllGroup()
        }

        if(typedisplayGroup.equals("groupMyJoin")){
            txtTitle!!.text="Nhóm bạn đã tham gia"
            getAllParticipant()



        }



    }
    fun initViewData(){


        rcvListGroup = findViewById(R.id.rcvListGroup)
        noGroupLayout = findViewById(R.id.noGroupLayou)
        txtTitle = findViewById(R.id.txtTitle)
        myLoader = findViewById(R.id.myLoader)
        groupAPI = Constrain.createRetrofit(GroupAPI::class.java)
        participantAPI = Constrain.createRetrofit(ParticipantAPI::class.java)

        searchView=findViewById(R.id.searchView)

        var intent=intent
        typedisplayGroup=intent.getStringExtra("group")

    }
    fun displayAllGroup() {
        myLoader!!.visibility=View.VISIBLE

        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler() { coroutineContext, throwable ->
            throwable.printStackTrace()
            Constrain.showToast(context,"Error connection")
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
                            }
                            if(listGroup!!.size==0){
                                noGroupLayout!!.visibility= View.VISIBLE
                                myLoader!!.visibility=View.GONE

                            }else{
                                noGroupLayout!!.visibility= View.GONE
                                groupAdapter =
                                GroupAdapter(context, listGroup!!, participantAPI!!,groupAPI!!)
                                rcvListGroup!!.adapter = groupAdapter
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
    fun getAllParticipant(){

        var countid=0
        myLoader!!.visibility=View.VISIBLE
        participantAPI!!.getAllParticipant()
            .enqueue(object : Callback<List<Participant>> {
                override fun onResponse(
                    call: Call<List<Participant>>,
                    response: Response<List<Participant>>
                ) {
                    if (response.code() == 200) {
                        var    listP = response.body()!!
                        Log.d("sizep", listP!!.size.toString())
                        for (i in listP!!.indices) {
                            Log.d("groupid",listP!![i].groupId)
                            if(listP!![i].uid.equals(CourseTypeActivity.uid)){ // lấy ra tất cả nhóm có userid là người đang đăng nhập
                                var idG=listP[i].groupId
                                countid++
                                getALGroupById(idG)
                            }
                        }
                        Log.e("count",countid.toString())
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
    private fun getALGroupById(idG: String) {

        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Constrain.showToast(context, "Data error")
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            groupAPI!!.getGroupById(idG)
                .enqueue(object : Callback<List<Group>> {
                    override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                        if(response.isSuccessful){
                            listG!!.addAll(response.body()!!)
                            Log.d("sizetest", listG!!.size.toString())

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

        }



    }



}