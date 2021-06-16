package com.example.suportstudy.controller

import android.util.Log
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import com.example.suportstudy.service.GroupAPI
import com.example.suportstudy.service.ParticipantAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ParticipantController {
    fun getGroupParticipanted(groupAPI: GroupAPI, participantAPI: ParticipantAPI,courseId:String):ArrayList<Group> {
        var listGroup = listOf<Group>()
        var listGroupAdapter = ArrayList<Group>()
        var listGroupParticipanted = ArrayList<Group>()
        groupAPI!!.getAllGroup()
            .enqueue(object : retrofit2.Callback<List<Group>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Group>>,
                    response: Response<List<Group>>
                ) {
                    if (response.code() == 200) {
                        listGroup = response.body()!!

                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Group>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })
        participantAPI!!.getAllParticipant()
            .enqueue(object : retrofit2.Callback<List<Participant>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Participant>>,
                    response: Response<List<Participant>>
                ) {
                    if (response.code() == 200) {
                        var listP = response.body()
                        for (i in listGroup!!.indices) {
                            if (listGroup!![i].courseId.equals(courseId)) {
                                listGroupAdapter.add(listGroup!![i])
                            }
                        }
                        for (i in listP!!.indices) {
                            if (!listP[i].uid.equals(ListCourseActivity.uid)) {
                                var idG = listP[i].groupId
                                listGroupParticipanted=getGroup(idG,groupAPI)
                            }
                        }
                    }
                }
                override fun onFailure(call: retrofit2.Call<List<Participant>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })


        return listGroupParticipanted

    }
    fun getGroup(id: String,groupAPI:GroupAPI):ArrayList<Group> {
        var list=ArrayList<Group>()

        groupAPI!!.getGroupById(id)
            .enqueue(object : Callback<List<Group>> {
                override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                    list!!.addAll(response.body()!!)
                }
                override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                }
            })
        return list

    }
}