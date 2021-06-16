package com.example.suportstudy.activity.group

import android.app.Dialog
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
import com.example.suportstudy.until.Until
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListGroupActivity : AppCompatActivity() {
    var context = this@ListGroupActivity

    var listP:List<Participant>?=ArrayList<Participant>()

    var groupAPI: GroupAPI? = null
    var participantAPI: ParticipantAPI? = null
    var rcvListGroup:RecyclerView?=null;
     var noGroupLayout:LinearLayout?=null;
     var groupAdapter:GroupAdapter?=null;
    var myLoader:LazyLoader?=null
    companion object {
        var imageUrl = ""
        var courseId= CourseDetailActivity.courseId.toString()
    }
    var listGroup: ArrayList<Group>? = ArrayList<Group>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_group)
        rcvListGroup = findViewById(R.id.rcvListGroup)
         noGroupLayout = findViewById(R.id.noGroupLayou)
        myLoader = findViewById(R.id.myLoader)
        groupAPI = Until.createRetrofit(GroupAPI::class.java)
        participantAPI = Until.createRetrofit(ParticipantAPI::class.java)



        displayListGroup()
    }
    fun displayListGroup() {
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler() { coroutineContext, throwable ->
            throwable.printStackTrace()
            Toast.makeText(this, "Errorconect", Toast.LENGTH_SHORT).show()
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            myLoader!!.visibility=View.VISIBLE
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
    fun test() {
        participantAPI!!.getAllParticipant()
            .enqueue(object : Callback<List<Participant>> {
                override fun onResponse(
                    call: Call<List<Participant>>,
                    response: Response<List<Participant>>
                ) {
                    if (response.code() == 200) {
                        listP = response.body()
                        for (i in listP!!.indices) {
                            if (listP!![i].uid.equals(ListCourseActivity.uid) && listP!![i].courseId.equals(
                                    CourseDetailActivity.courseId
                                ) ) {
                                var idG = listP!![i].groupId

                                getGroup(idG)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })
    }
    fun getGroup(idG: String) {
        Log.d("id", idG)
        CourseDetailActivity.listG!!.clear()
        groupAPI!!.getGroupById(idG)
            .enqueue(object : Callback<List<Group>> {
                override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                    CourseDetailActivity.listG!!.addAll(response.body()!!)
                    Log.d("sizetest", CourseDetailActivity.listG!!.size.toString())
                }
                override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                }
            })

    }
}