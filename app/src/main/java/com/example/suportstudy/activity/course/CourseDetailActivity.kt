package com.example.suportstudy.activity.course

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.activity.group.ListGroupActivity
import com.example.suportstudy.activity.home.HomeActivity
import com.example.suportstudy.model.Participant
import com.example.suportstudy.service.GroupAPI
import com.example.suportstudy.service.ParticipantAPI
import com.example.suportstudy.until.Constrain
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseDetailActivity : AppCompatActivity() {

    var txtCourseName: TextView? = null
    var txtDescription: TextView? = null
    var courseIv: ImageView? = null
    var btnJoin: Button? = null
    var btnGroup: Button? = null
    var context = this@CourseDetailActivity

    var groupAPI: GroupAPI? = null
    var participantAPI: ParticipantAPI? = null

    var myUid=CourseTypeActivity.uid

    var sd:SweetAlertDialog?=null

    companion object {
        var imageUrl = ""
        var courseId: String? = null
    }


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)
        sd=Constrain.sweetdialog(context,"Đang tạo nhóm...")
        groupAPI = Constrain.createRetrofit(GroupAPI::class.java)
        participantAPI = Constrain.createRetrofit(ParticipantAPI::class.java)


        txtCourseName = findViewById(R.id.txtCourseName)
        txtDescription = findViewById(R.id.txtDescription)
        courseIv = findViewById(R.id.courseIv)
        btnJoin = findViewById(R.id.btnJoin)
        btnGroup = findViewById(R.id.btnCrearteClass)



        if (CourseTypeActivity.istutor == true) {
            btnGroup!!.text = "Tạo Nhóm"
            btnGroup!!.setOnClickListener {
                createGroup()
            }
        } else {
            btnGroup!!.text = "Nhóm thảo luận"
            btnGroup!!.setOnClickListener {
                var intent=Intent(context,ListGroupActivity::class.java)
                intent.putExtra("group","allgroup")
                startActivity(intent)
            }
        }
        loadDetail()
        btnJoin!!.setOnClickListener {
            var intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    fun loadDetail() {
        var intent: Intent = getIntent()
        courseId = intent.getStringExtra("courseId")
        var name = intent.getStringExtra("name")
        var desciption = intent.getStringExtra("desciption")
        imageUrl = intent.getStringExtra("image").toString()

        txtCourseName!!.text = name
        txtDescription!!.text = desciption

        Constrain.checkShowImage(context, imageUrl,courseIv!!)

    }
    fun createGroup() {
        val dialog =Constrain.createDialog(context,R.layout.dialog_create_group)
        val btnHuy = dialog!!.findViewById<Button>(R.id.btnHuy)
        val btnTao = dialog!!.findViewById<Button>(R.id.btnTao)
        val edtName = dialog!!.findViewById<EditText>(R.id.edtName)
        val edtDecription = dialog!!.findViewById<EditText>(R.id.edtDecription)
        val ivGroup = dialog!!.findViewById<CircleImageView>(R.id.IVGroup)

        btnTao.setOnClickListener {
            sd!!.show()
            var groupName = edtName.text.toString()
            var groupDescription = edtDecription.text.toString()
            var time=System.currentTimeMillis().toString()
            groupAPI!!.insertGroup(
                myUid,
                groupName,
                groupDescription,
                "",
                courseId!!
            )
                .enqueue(object : retrofit2.Callback<com.example.suportstudy.model.Group> {
                    override fun onResponse(
                        call: retrofit2.Call<com.example.suportstudy.model.Group>,
                        response: Response<com.example.suportstudy.model.Group>
                    ) {
                        if (response.isSuccessful) {
                            var groupId= response.body()!!._id
                            participantAPI!!.insertParticipant(
                               time,
                                myUid,
                                groupId!!,
                                courseId!!
                            ).enqueue(object :Callback<Participant>{
                                override fun onResponse(
                                    call: Call<Participant>,
                                    response: Response<Participant>
                                ) {
                                   if(response.isSuccessful){
                                       Constrain.showToast(context,"Tạo nhóm thành công")
                                       sd!!.dismiss()
                                       dialog.dismiss()
                                   }
                                }

                                override fun onFailure(call: Call<Participant>, t: Throwable) {
                                }

                            })
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<com.example.suportstudy.model.Group>, t: Throwable) {

                        Log.v("Data", "Error: " + t.message.toString())
                    }
                })

        }
        btnHuy.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    override fun onBackPressed() {
        Constrain.showToast(context, "fff")
        super.onBackPressed()
    }
    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp()
        Constrain.showToast(context, "fff")
    }
}