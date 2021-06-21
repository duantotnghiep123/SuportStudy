package com.example.suportstudy.activity.acount

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.suportstudy.R
import com.example.suportstudy.activity.MainActivity
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.activity.group.ListGroupActivity
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.UserAPI
import com.example.suportstudy.until.Constrain
import com.example.suportstudy.until.Persmission
import com.google.gson.annotations.Until
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_test_image.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileActivity : AppCompatActivity() {
    val context=this@ProfileActivity
    var listGroupLayout:RelativeLayout?=null
    var changeNameLayout:RelativeLayout?=null
    var logoutLayout:RelativeLayout?=null
    var avatarIv:CircleImageView?=null
    var nameTv:TextView?=null

    var sharedPreferences: SharedPreferences? = null
    var userAPI:UserAPI?=null

    var image_uri: Uri? = null
    var part_image: String? = null


    var image=""
    var email=""
    var password=""
    var name=""
    var key=""


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViewData()
        key="loadprofile"

        avatarIv!!.setOnClickListener {
            key="editimage"
            if (!Persmission.checkStoragePermission(context)) {
                Persmission.requestStoragetPermission(context)
            } else {
                Persmission.pickFromGallery(context)
            }
        }
        getDataProfile(ListCourseActivity.uid)



        listGroupLayout!!.setOnClickListener {
            var intent=Intent(context,ListGroupActivity::class.java)
            intent.putExtra("group","groupMyJoin")
            startActivity(intent)
        }
        changeNameLayout!!.setOnClickListener {
            key="editname"
            getDataProfile(ListCourseActivity.uid)
        }
        logoutLayout!!.setOnClickListener {
            val editor = sharedPreferences!!.edit()
            editor.clear()
            editor.commit()
            startActivity(Intent(context,MainActivity::class.java))
            finish()
        }
    }
    fun initViewData(){
        sharedPreferences = getSharedPreferences(Constrain.SHARED_REF_NAME, MODE_PRIVATE)

        listGroupLayout=findViewById(R.id.listGroupLayout)
        changeNameLayout=findViewById(R.id.changeNameLayout)
        logoutLayout=findViewById(R.id.logoutLayout)
        avatarIv=findViewById(R.id.avatarIv)
        nameTv=findViewById(R.id.nameTv)
        userAPI= Constrain.createRetrofit(UserAPI::class.java)

    }
    fun editImage(){
        var file= File(part_image)
        var requestId=RequestBody.create(MediaType.parse("multipart/form_data"),ListCourseActivity.uid)
        var requestOldImage=RequestBody.create(MediaType.parse("multipart/form_data"),image)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("image", file.getName(), reqFile)
       userAPI!!.editImage(requestId,body, requestOldImage)
           .enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                Log.d("UploadImage", response.body().toString())
                if (response.isSuccessful) {
                    Constrain.showToast(context,"Đổi thành công")

                    getDataProfile(ListCourseActivity.uid)
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Constrain.showToast(context,"Thất bại")

                t.printStackTrace()
                Log.e("ERROR", t.toString())
            }
        })


    }

    private fun getDataProfile(uid: String?) {
              userAPI!!.getAllUsersByID(uid).enqueue(object :Callback<List<Users>>{
                  override fun onResponse(
                      call: Call<List<Users>>,
                      response: Response<List<Users>>
                  ) {
                      if(response.isSuccessful){
                          var imgUrl=""

                          for (i in response.body()!!.indices){
                               imgUrl= response.body()!![i].image
                               name= response.body()!![i].name
                               password= response.body()!![i].password

                          }
                          if (key.equals("editimage")){

                          }
                          Constrain.showToast(context,image)
                          val editor = sharedPreferences!!.edit()
                          editor.putString(Constrain.KEY_IMAGE, imgUrl)
                          editor.apply()

                          image = sharedPreferences!!.getString(Constrain.KEY_IMAGE, "noImage")!!
                          Constrain.showToast(context,"image"+image)
                          nameTv!!.text=name
                          if(imgUrl.equals("noImage") ||imgUrl.equals("")){
                              avatarIv!!.setImageResource(R.drawable.loginimage)
                          }else{
                              var path=Constrain.baseUrl+"/profile/"+imgUrl.substring(30)
                              Picasso.with(context).load(path).into(avatarIv)
                          }


                      }

                  }

                  override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                      Log.e("err",t.message.toString())
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
                        Constrain.showToast(context, "Bật quyen thư viện")
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode ==Persmission.IMAGE_PICK_GALLERY_CODE) {
                image_uri = data!!.data!!
                part_image=  getRealPathFromURI(image_uri)
                Constrain.showToast(context,part_image!!)

                editImage()

            }



        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun getRealPathFromURI(contentUri: Uri?): String? {

        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(
            contentUri,
            proj,  // Which columns to return
            null,  // WHERE clause; which rows to return (all rows)
            null,  // WHERE clause selection arguments (none)
            null
        ) // Order-by clause (ascending by name)
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
}