package com.example.suportstudy.activity.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseDetailActivity
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.adapter.GroupAdapter
import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
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

    var listGroup: ArrayList<Group>? = ArrayList<Group>()
    var typedisplayGroup:String?=null
    var listP:List<Participant>?=ArrayList<Participant>()
    var listG:ArrayList<Group>?=ArrayList<Group>() //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_group)
        rcvListGroup = findViewById(R.id.rcvListGroup)
        noGroupLayout = findViewById(R.id.noGroupLayou)
        myLoader = findViewById(R.id.myLoader)
        groupAPI = Constrain.createRetrofit(GroupAPI::class.java)
        participantAPI = Constrain.createRetrofit(ParticipantAPI::class.java)

        var intent=intent
        typedisplayGroup=intent.getStringExtra("group")

        if(typedisplayGroup.equals("allgroup")){
            displayAllGroup()
        }
        if(typedisplayGroup.equals("groupMyJoin")){
            getAllParticipant()
        }


    }
    fun displayAllGroup() {
        myLoader!!.visibility=View.VISIBLE

        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler() { coroutineContext, throwable ->
            throwable.printStackTrace()
            Toast.makeText(this, "Errorconect", Toast.LENGTH_SHORT).show()
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
                            if(listP!![i].uid.equals(ListCourseActivity.uid)){ // lấy ra tất cả nhóm có userid là người đang đăng nhập
                                var idG=listP[i].groupId
                                getALGroupById(idG)
                            }

                        }

                    }


                }
                override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })


    }
    private fun getALGroupById(idG: String) {
        listG!!.clear()
        groupAPI!!.getGroupById(idG)
            .enqueue(object : Callback<List<Group>> {
                override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                    listG!!.addAll(response.body()!!)
                    Log.d("sizetest", listG!!.size.toString())

                    groupAdapter =   GroupAdapter(context, listG!!, participantAPI!!,groupAPI!!)
                    rcvListGroup!!.adapter = groupAdapter
                    rcvListGroup!!.visibility=View.VISIBLE
                    myLoader!!.visibility=View.GONE
                }

                override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                }
            })

    }



}