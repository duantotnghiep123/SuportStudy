package com.example.suportstudy.controller

import android.app.Dialog
import android.util.Log
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.adapter.GroupAdapter
import com.example.suportstudy.service.GroupAPI
import retrofit2.Response
import java.security.acl.Group

object GroupController {
     fun createGroup2(groupAPI: GroupAPI,createBy:String,groupName:String,groupDescription:String,groupImage:String,courseId:String,dialog: Dialog){
         groupAPI!!.insertGroup(
             createBy,
             groupName,
             groupDescription,
             groupImage,
             courseId!!
         )
             .enqueue(object : retrofit2.Callback<com.example.suportstudy.model.Group> {
                 override fun onResponse(
                     call: retrofit2.Call<com.example.suportstudy.model.Group>,
                     response: Response<com.example.suportstudy.model.Group>
                 ) {
                     if (response.isSuccessful) {
                         dialog.dismiss()
                     }
                 }

                 override fun onFailure(call: retrofit2.Call<com.example.suportstudy.model.Group>, t: Throwable) {

                     Log.v("Data", "Error: " + t.message.toString())
                 }
             })
     }
  suspend   fun getAllGroupByCourse(groupAPI: GroupAPI,courseId:String): ArrayList<com.example.suportstudy.model.Group> {
        var listGroup = ArrayList<com.example.suportstudy.model.Group>()

        groupAPI!!.getAllGroup()
            .enqueue(object : retrofit2.Callback<List<com.example.suportstudy.model.Group>> {
                override fun onResponse(
                    call: retrofit2.Call<List<com.example.suportstudy.model.Group>>,
                    response: Response<List<com.example.suportstudy.model.Group>>
                ) {
                    if (response.code() == 200) {
                        var list = response.body()
                        for (i in list!!.indices) {
                            if (list[i].courseId.equals(courseId)) {
                                listGroup.add(list[i])
                            }
                        }
                        Log.d("size",listGroup.size.toString()+"")
                    }
                }
                override fun onFailure(call: retrofit2.Call<List<com.example.suportstudy.model.Group>>, t: Throwable) {
                    Log.v("Data", "Error:" + t.message.toString())
                }
            })
        return listGroup
    }
}