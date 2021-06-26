package com.example.suportstudy.activity.acount

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.example.suportstudy.R
import com.example.suportstudy.activity.MainActivity
import com.example.suportstudy.activity.course.CourseTypeActivity
import com.example.suportstudy.activity.group.ListGroupActivity
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.UserAPI
import com.example.suportstudy.until.Constrain
import com.example.suportstudy.until.Persmission
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_test_image.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileActivity : AppCompatActivity() {
    val context = this@ProfileActivity
    var listGroupLayout: RelativeLayout? = null
    var changeNameLayout: RelativeLayout? = null
    var changepasswordLayout: RelativeLayout? = null
    var deletedUserLayout: RelativeLayout? = null
    var logoutLayout: RelativeLayout? = null
    var avatarIv: CircleImageView? = null
    var nameTv: TextView? = null
    var finishTv: TextView? = null

    var sharedPreferences: SharedPreferences? = null
    var userAPI: UserAPI? = null

    var image_uri: Uri? = null
    var part_image: String? = null


    var image = ""
    var email = ""
    var password = ""
    var name = ""
    var key = ""


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViewData()
        getDataProfile(CourseTypeActivity.uid)
        finishTv!!.setOnClickListener {
            Constrain.nextActivity(context,CourseTypeActivity::class.java)
            finish()
        }

        avatarIv!!.setOnClickListener {
            if (!Persmission.checkStoragePermission(context)) {
                Persmission.requestStoragetPermission(context)
            } else {
                Persmission.pickFromGallery(context)
            }
        }

        listGroupLayout!!.setOnClickListener {
            var intent = Intent(context, ListGroupActivity::class.java)
            intent.putExtra("group", "groupMyJoin")
            startActivity(intent)
        }
        changeNameLayout!!.setOnClickListener {
            editName()
        }
        changepasswordLayout!!.setOnClickListener {
            editPassword()
        }
        deletedUserLayout!!.setOnClickListener {
            deleteUser()
        }

        logoutLayout!!.setOnClickListener {

            val dialog = Constrain.createDialog(context,R.layout.dialog_confirm)

            var txtXacNhan = dialog.findViewById<TextView>(R.id.txtXacNhan)
            var btnHuy = dialog.findViewById<AppCompatButton>(R.id.btnHuy)
            var btnXacNhan = dialog.findViewById<AppCompatButton>(R.id.btnXacNhan)
            txtXacNhan.setText("Bạn có muốn đăng xuất !")
            btnXacNhan.setOnClickListener {
                dialog.dismiss()
                val editor = sharedPreferences!!.edit()
                editor.clear()
                editor.commit()
                startActivity(Intent(context, MainActivity::class.java))
                finish()
            }
            btnHuy.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()

        }
    }



    fun initViewData() {
        sharedPreferences = getSharedPreferences(Constrain.SHARED_REF_NAME, MODE_PRIVATE)

        listGroupLayout = findViewById(R.id.listGroupLayout)
        changeNameLayout = findViewById(R.id.changeNameLayout)
        changepasswordLayout = findViewById(R.id.changepasswordLayout)
        deletedUserLayout = findViewById(R.id.deletedUserLayout)
        logoutLayout = findViewById(R.id.logoutLayout)
        avatarIv = findViewById(R.id.avatarIv)
        nameTv = findViewById(R.id.nameTv)
        finishTv = findViewById(R.id.finishTv)
        userAPI = Constrain.createRetrofit(UserAPI::class.java)

    }

    fun editImage() {
        var file = File(part_image)
        var requestId =
            RequestBody.create(MediaType.parse("multipart/form_data"), CourseTypeActivity.uid)
        var requestOldImage = RequestBody.create(MediaType.parse("multipart/form_data"), image)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("image", file.getName(), reqFile)
        userAPI!!.editImage(requestId, body, requestOldImage)
            .enqueue(object : Callback<Users> {
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    Log.d("UploadImage", response.body().toString())
                    if (response.isSuccessful) {
                        Constrain.showToast(context, "Đổi thành công")
                        getDataProfile(CourseTypeActivity.uid)
                    }
                }
                override fun onFailure(call: Call<Users>, t: Throwable) {
                    Constrain.showToast(context, "Thất bại")

                    t.printStackTrace()
                    Log.e("ERROR", t.toString())
                }
            })


    }

    private fun getDataProfile(uid: String?) {
        userAPI!!.getAllUsersByID(uid).enqueue(object : Callback<List<Users>> {
            override fun onResponse(
                call: Call<List<Users>>,
                response: Response<List<Users>>
            ) {
                if (response.isSuccessful) {
                    var imgUrl = ""
                    for (i in response.body()!!.indices) {
                        imgUrl = response.body()!![i].image
                        name = response.body()!![i].name
                        password = response.body()!![i].password

                    }
                    Constrain.showToast(context, image)
                    val editor = sharedPreferences!!.edit()
                    editor.putString(Constrain.KEY_IMAGE, imgUrl)
                    editor.apply()

                    image = sharedPreferences!!.getString(Constrain.KEY_IMAGE, "noImage")!!
                    nameTv!!.text = name
                    if (imgUrl.equals("noImage") || imgUrl.equals("")) {
                        avatarIv!!.setImageResource(R.drawable.loginimage)
                    } else {
                        var path = Constrain.baseUrl + "/profile/" + imgUrl.substring(30)
                        Picasso.with(context).load(path).into(avatarIv)
                    }

                }

            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Log.e("err", t.message.toString())
            }

        })
    }

    fun editName() {
        val dialog = Constrain.createDialog(context,R.layout.dialog_edit_name)
        val edtName = dialog!!.findViewById<EditText>(R.id.edtName)
        val btnDoi = dialog!!.findViewById<Button>(R.id.btnDoi)
        val btnHuy = dialog!!.findViewById<Button>(R.id.btnHuy)
        edtName.setText(name)
        btnDoi.setOnClickListener {
            var name = edtName.text.toString()
            if (name.equals("")) {
                Constrain.showToast(context, "Vui lòng nhập tên")
            } else {
                userAPI!!.editName(CourseTypeActivity.uid, name)
                    .enqueue(object : Callback<Users> {
                        override fun onResponse(call: Call<Users>, response: Response<Users>) {
                            if (response.isSuccessful) {
                                Constrain.showToast(context, "Đổi thành công")
                                getDataProfile(CourseTypeActivity.uid)
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<Users>, t: Throwable) {
                            Constrain.showToast(context, "Đổi thất bại")
                            dialog.dismiss()
                            Log.e("err", t.message.toString())
                        }

                    })
            }


        }
        btnHuy.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun editPassword() {
        val dialog = Constrain.createDialog(context,R.layout.dialog_edit_password)

        var edtNowPassword = dialog.findViewById<EditText>(R.id.edtNowPassword)
        var edtNewPassword = dialog.findViewById<EditText>(R.id.edtNewPassword)
        var edtCfNewPassword = dialog.findViewById<EditText>(R.id.edtCfNewPassword)
        var btnHuy = dialog.findViewById<AppCompatButton>(R.id.btnHuy)
        var btnXacNhan = dialog.findViewById<AppCompatButton>(R.id.btnXacNhan)
        btnXacNhan.setOnClickListener {
            var oldpassword = edtNowPassword.text.toString()
            var newpassword = edtNewPassword.text.toString()
            var newcfpassword = edtCfNewPassword.text.toString()
            if (oldpassword.equals("")) {
                Constrain.showToast(context, "Nhập mật khẩu cũ")
            } else if (newpassword.equals("")) {
                Constrain.showToast(context, "Nhập mật khẩu mới")
            } else if (newcfpassword.equals("")) {
                Constrain.showToast(context, "Nhập mật khẩu xác nhận")
            } else if (!oldpassword.equals(password)) {
                Constrain.showToast(context, "Mật khẩu cũ không đúng")
            } else if (!newpassword.equals(newcfpassword)) {
                Constrain.showToast(context, "Mật khẩu xác nhận không đúng")
            } else {
                userAPI!!.editPassword(CourseTypeActivity.uid, newpassword)
                    .enqueue(object : Callback<Users> {
                        override fun onResponse(call: Call<Users>, response: Response<Users>) {
                            if (response.isSuccessful) {
                                Constrain.showToast(context, "Đổi thành công")
                                getDataProfile(CourseTypeActivity.uid)
                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<Users>, t: Throwable) {
                            Constrain.showToast(context, "Đổi thất bại")
                            dialog.dismiss()
                            Log.e("err", t.message.toString())
                        }

                    })
            }
        }
        btnHuy.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
    private fun deleteUser() {
        val dialog = Constrain.createDialog(context,R.layout.dialog_confirm)

        var txtXacNhan = dialog.findViewById<TextView>(R.id.txtXacNhan)
        var btnHuy = dialog.findViewById<AppCompatButton>(R.id.btnHuy)
        var btnXacNhan = dialog.findViewById<AppCompatButton>(R.id.btnXacNhan)
        txtXacNhan.setText("Bạn có muốn xóa tài khoản của bạn !")
        btnXacNhan.setOnClickListener {
        }
        btnHuy.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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
            if (requestCode == Persmission.IMAGE_PICK_GALLERY_CODE) {
                image_uri = data!!.data!!
                part_image = Constrain.getRealPathFromURI(context,image_uri)
                Constrain.showToast(context, part_image!!)
                editImage()

            }


        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}