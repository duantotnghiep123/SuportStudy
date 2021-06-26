package com.example.suportstudy.activity.group

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.CourseTypeActivity
import com.example.suportstudy.model.Course
import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import com.example.suportstudy.service.CourseAPI
import com.example.suportstudy.service.GroupAPI
import com.example.suportstudy.service.ParticipantAPI
import com.example.suportstudy.until.Constrain
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoGroupActivity : AppCompatActivity() {
    var context = this@InfoGroupActivity
    var myUid = CourseTypeActivity.uid
    var participantAPI: ParticipantAPI? = null
    var groupAPI: GroupAPI? = null

    companion object {
        var groupCreateBy: String? = null
    }

    var groupId: String? = null
    var groupName: String? = null
    var groupDescription: String? = null
    var groupImage: String? = null

    var groupIv: ImageView? = null
    var groupNameTv: TextView? = null
    var groupDescriptionTv: TextView? = null
    var leaveGroupTv: TextView? = null
    var changeGroupNameLayout: RelativeLayout? = null
    var viewMemberLayout: RelativeLayout? = null
    var leaveGroupLayout: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_info)
        initViewData()

        viewMemberLayout!!.setOnClickListener {
            var intent = Intent(context, ListMemberGroupActivity::class.java)
            intent.putExtra("groupId", groupId)
            context.startActivity(intent)
        }
        leaveGroupLayout!!.setOnClickListener {
            var dialog=Constrain.createDialog(context,R.layout.dialog_confirm)
            var txtXacnhan=dialog.txtXacNhan
            if(leaveGroupTv!!.text.equals("Rời nhóm")){
                txtXacnhan.text="Bạn muốn rời nhóm ?"
            }
            else if(leaveGroupTv!!.text.equals("Xóa nhóm")){
                txtXacnhan.text="Bạn muốn xóa nhóm ?"
            }
            var btnHuy=dialog.btnHuy
            var btnXacNhan=dialog.btnXacNhan

            btnHuy.setOnClickListener { dialog.dismiss() }
            btnXacNhan.setOnClickListener {
                if (leaveGroupTv!!.text.equals("Rời nhóm")) {
                    participantAPI!!.getAllParticipant().enqueue(object : Callback<List<Participant>> {
                        override fun onResponse(
                            call: Call<List<Participant>>,
                            response: Response<List<Participant>>
                        ) {
                            if (response.isSuccessful) {
                                var list = response.body()
                                var idpartincipant = ""
                                for (i in list!!.indices) {
                                    if (list[i].uid.equals(myUid) && list[i].groupId.equals(groupId)) {
                                        idpartincipant = list[i]._id
                                        break
                                    }
                                }
                                participantAPI!!.leaveGroup(idpartincipant)
                                    .enqueue(object : Callback<Participant> {
                                        override fun onResponse(
                                            call: Call<Participant>,
                                            response: Response<Participant>
                                        ) {

                                            if (response.isSuccessful) {
                                                Constrain.nextActivity(
                                                    context,
                                                    CourseTypeActivity::class.java
                                                )
                                                finish()
                                            }
                                        }
                                        override fun onFailure(call: Call<Participant>, t: Throwable) {
                                            Log.e("err", t.message.toString())
                                        }
                                    })
                            }
                        }
                        override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                            Log.e("err", t.message.toString())

                        }

                    })

                } else if (leaveGroupTv!!.text.equals("Xóa nhóm")) {
                    participantAPI!!.getAllParticipant().enqueue(object : Callback<List<Participant>> {
                        override fun onResponse(
                            call: Call<List<Participant>>,
                            response: Response<List<Participant>>
                        ) {
                            if (response.isSuccessful) {
                                var list = response.body()
                                var idpartincipant = ""
                                for (i in list!!.indices) {
                                    if (list[i].groupId.equals(groupId)) {
                                        idpartincipant = list[i]._id
                                        deleteParticipantByID(idpartincipant)
                                    }
                                }
                                Log.e("groupId", groupId!!)
                                groupAPI!!.deleteGroup(groupId)
                                    .enqueue(object :Callback<Group>{
                                        override fun onResponse(
                                            call: Call<Group>,
                                            response: Response<Group>
                                        ) {
                                            if (response.isSuccessful) {
                                                Constrain.nextActivity(
                                                    context,
                                                    CourseTypeActivity::class.java
                                                )
                                                finish()
                                            }                                    }

                                        override fun onFailure(call: Call<Group>, t: Throwable) {
                                            Log.e("err", t.message.toString())
                                        }

                                    })


                            }
                        }

                        override fun onFailure(call: Call<List<Participant>>, t: Throwable) {
                            Log.e("err", t.message.toString())

                        }

                    })
                }
                dialog.dismiss()
            }
             dialog.show()

        }
        changeGroupNameLayout!!.setOnClickListener {
            updateGroupName()
        }
    }

    private fun updateGroupName() {
       val dialog= Constrain.createDialog(context,R.layout.dialog_edit_name)
        val edtName=dialog.findViewById<EditText>(R.id.edtName)
        val btnHuy=dialog.findViewById<AppCompatButton>(R.id.btnHuy)
        val btnDoi=dialog.findViewById<AppCompatButton>(R.id.btnDoi)
        edtName.setText(groupName)
        btnDoi.setOnClickListener {
            var groupName=edtName.text.toString()
            if(groupName.equals("")){
                Constrain.showToast(context,"Vui lòng nhập tên nhóm")
            }else{
                groupAPI!!.updateGroupName(groupId,groupName).enqueue(object :Callback<Group>{
                    override fun onResponse(call: Call<Group>, response: Response<Group>) {
                        if(response.isSuccessful){
                           getGroupById()
                            dialog.dismiss()
                        }
                    }

                    override fun onFailure(call: Call<Group>, t: Throwable) {
                        Log.e("err", t.message.toString())
                    }

                })
            }

        }

        btnHuy.setOnClickListener { dialog.dismiss() }
        dialog.show()



    }

    fun initViewData() {
        groupIv = findViewById(R.id.groupImage)
        groupNameTv = findViewById(R.id.groupNameTv)
        groupDescriptionTv = findViewById(R.id.groupDescriptionTv)
        leaveGroupTv = findViewById(R.id.leaveGroupTv)
        changeGroupNameLayout = findViewById(R.id.changeGroupNameLayout)
        viewMemberLayout = findViewById(R.id.viewMemberLayout)
        leaveGroupLayout = findViewById(R.id.leaveGroupLayout)

        participantAPI = Constrain.createRetrofit(ParticipantAPI::class.java)
        groupAPI = Constrain.createRetrofit(GroupAPI::class.java)


        var intentGroupChat = intent
        groupId = intentGroupChat.getStringExtra("groupId")
        groupCreateBy = intentGroupChat.getStringExtra("groupCreateBy")
        groupName = intentGroupChat.getStringExtra("groupName")
        groupDescription = intentGroupChat.getStringExtra("groupDescription")
        groupImage = intentGroupChat.getStringExtra("groupImage")


        if (!groupCreateBy.equals(CourseTypeActivity.uid)) {
            leaveGroupTv!!.text = "Rời nhóm"
        } else {
            leaveGroupTv!!.text = "Xóa nhóm"
        }
        getGroupById()


    }

    fun getGroupById(){
        val chatFetchJob = Job()
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Constrain.showToast(context, "Data error")
        }
        val scope = CoroutineScope(chatFetchJob + Dispatchers.Main)
        scope.launch(errorHandler) {
            groupAPI!!.getGroupById(groupId)
                .enqueue(object :Callback<List<Group>>{
                    override fun onResponse(
                        call: Call<List<Group>>,
                        response: Response<List<Group>>
                    ) {
                        for (i in response.body()!!.indices){
                            groupNameTv!!.text = response.body()!![i].groupName
                            groupDescriptionTv!!.text = response.body()!![i].groupDescription
                            var imageUrl=response.body()!![i].groupImage

                            Constrain.checkShowImage(context, imageUrl!!, groupIv!!)
                        }
                    }
                    override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                        Log.e("err", t.message.toString())
                    }

                })

        }

    }

    fun deleteParticipantByID(idpartincipant:String) {
        participantAPI!!.leaveGroup(idpartincipant).enqueue(object : Callback<Participant> {
            override fun onResponse(
                call: Call<Participant>,
                response: Response<Participant>
            ) {
                Constrain.showToast(context, idpartincipant)

                if (response.isSuccessful) {

                }
            }

            override fun onFailure(call: Call<Participant>, t: Throwable) {
                Log.e("err", t.message.toString())
            }

        })
    }
}