package com.example.suportstudy.activity.course

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.activity.group.ListGroupActivity
import com.example.suportstudy.activity.home.HomeActivity
import com.example.suportstudy.model.Group
import com.example.suportstudy.model.Participant
import com.example.suportstudy.service.GroupAPI
import com.example.suportstudy.service.ParticipantAPI
import com.example.suportstudy.until.Constrain
import com.example.suportstudy.until.Persmission
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CourseDetailActivity : AppCompatActivity() {

    var txtCourseName: TextView? = null
    var txtDescription: TextView? = null
    var courseIv: ImageView? = null
    var btnJoin: Button? = null
    var btnCreateGroup: Button? = null
    var  ivGroup: CircleImageView? = null
    var context = this@CourseDetailActivity

    var groupAPI: GroupAPI? = null
    var participantAPI: ParticipantAPI? = null

    var myUid=CourseTypeActivity.uid

    var sd:SweetAlertDialog?=null
    var image_uri: Uri? = null
    var part_image: String? = null

    companion object {
        var imageUrl = ""
        var courseId: String? = null
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)
        initViewData()
        loadDetail()

        btnCreateGroup!!.setOnClickListener {
            if(btnCreateGroup!!.text.equals("Tạo Nhóm")){
                createGroup()
            }else if(btnCreateGroup!!.text.equals("Nhóm thảo luận")){
                var intent=Intent(context,ListGroupActivity::class.java)
                intent.putExtra("group","allgroup")
                startActivity(intent)
            }
        }

        btnJoin!!.setOnClickListener {
            var intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (CourseTypeActivity.istutor == true) {
            btnCreateGroup!!.text = "Tạo Nhóm"
        } else {
            btnCreateGroup!!.text = "Nhóm thảo luận"

        }

    }
    fun initViewData(){
        Constrain.context=context
        sd=Constrain.sweetdialog(context,"Đang tạo nhóm...")

        groupAPI = Constrain.createRetrofit(GroupAPI::class.java)
        participantAPI = Constrain.createRetrofit(ParticipantAPI::class.java)


        txtCourseName = findViewById(R.id.txtCourseName)
        txtDescription = findViewById(R.id.txtDescription)
        courseIv = findViewById(R.id.courseIv)
        btnJoin = findViewById(R.id.btnJoin)
        btnCreateGroup = findViewById(R.id.btnCrearteClass)

        if (CourseTypeActivity.istutor == true) {
            btnCreateGroup!!.text = "Tạo Nhóm"
        } else {
            btnCreateGroup!!.text = "Nhóm thảo luận"
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

        Constrain.checkShowImage(context,R.drawable.ic_gallery_grey, imageUrl,courseIv!!)

    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun createGroup() {
        val dialog =Constrain.createDialog(context,R.layout.dialog_create_group)
        val btnHuy = dialog!!.findViewById<Button>(R.id.btnHuy)
        val btnTao = dialog!!.findViewById<Button>(R.id.btnTao)
        val edtName = dialog!!.findViewById<EditText>(R.id.edtName)
        val edtDecription = dialog!!.findViewById<EditText>(R.id.edtDecription)
         ivGroup = dialog!!.findViewById<CircleImageView>(R.id.IVGroup)

        ivGroup!!.setOnClickListener {
            if (!Persmission.checkStoragePermission(context)) {
                Persmission.requestStoragetPermission(context)
            } else {
                Persmission.pickFromGallery(context)

            }
        }

        btnTao.setOnClickListener {
            sd!!.show()
            var groupName = edtName.text.toString()
            var groupDescription = edtDecription.text.toString()
            var time=System.currentTimeMillis().toString()
           if (image_uri==null){
               createGroupNoImage(myUid,groupName,groupDescription,"noImage",courseId,time)
               sd!!.dismiss()
               dialog.dismiss()
           }else{
               createGroupWithImage(myUid!!,groupName,groupDescription,time)
               sd!!.dismiss()
               dialog.dismiss()
           }




        }
        btnHuy.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createGroupWithImage(createBy:String,groupName:String,groupDescription:String,time:String) {
        var file = File(part_image)

        var createBy =   RequestBody.create(MediaType.parse("multipart/form_data"), createBy)
        var groupName =   RequestBody.create(MediaType.parse("multipart/form_data"), groupName)
        var groupDescription =   RequestBody.create(MediaType.parse("multipart/form_data"), groupDescription)
        var courseID =   RequestBody.create(MediaType.parse("multipart/form_data"), courseId)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("group", file.getName(), reqFile)

        groupAPI!!.createGroupWithImage(createBy,groupName,groupDescription,image,courseID)
            .enqueue(object : Callback<Group>{
                override fun onResponse(call: Call<Group>, response: Response<Group>) {
                    if (response.isSuccessful) {
                        var groupId= response.body()!!._id
                        participant(time!!, myUid!!, groupId!!, courseId!!)
                    }
                }
                override fun onFailure(call: Call<Group>, t: Throwable) {
                     Log.e("Error",t.message.toString())
                }
            } )

    }

    private fun createGroupNoImage(myUid: String?, groupName: String, groupDescription: String, image: String, courseId: String?, time: String?) {
        groupAPI!!.createGroupNoImage(
            myUid,
            groupName,
            groupDescription,
            image,
            courseId!!
        )
            .enqueue(object : Callback<Group> {
                override fun onResponse(
                    call: Call<Group>,
                    response: Response<Group>
                ) {
                    if (response.isSuccessful) {
                        var groupId= response.body()!!._id
                        participant(time!!, myUid!!, groupId!!,courseId!!)

                    }
                }
                override fun onFailure(call: retrofit2.Call<com.example.suportstudy.model.Group>, t: Throwable) {
                    Log.v("Data", "Error: " + t.message.toString())
                }
            })
    }
    fun participant(time:String,myUid:String,groupId:String,courseId:String){
        participantAPI!!.insertParticipant(
            time,
            myUid,
            groupId!!,
            courseId
        ).enqueue(object :Callback<Participant>{
            override fun onResponse(
                call: Call<Participant>,
                response: Response<Participant>
            ) {
                if(response.isSuccessful){
                    Constrain.showToast("Tạo nhóm thành công")
                    sd!!.dismiss()

                }
            }

            override fun onFailure(call: Call<Participant>, t: Throwable) {
                Log.e("Error",t.message.toString())

            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Persmission.STORAGE_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val writeStorageAccpted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (writeStorageAccpted) {
                        Persmission.pickFromGallery(context)
                    } else {
                        Constrain.showToast( "Bật quyền thư viện")
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Persmission.IMAGE_PICK_GALLERY_CODE) {
                image_uri = data!!.data!!
                ivGroup!!.setImageURI(image_uri)

                part_image = Constrain.getRealPathFromURI(context,image_uri)
                Log.e("imageUri", image_uri.toString())
            }


        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}