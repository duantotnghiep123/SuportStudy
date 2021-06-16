package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.activity.MainActivity
import com.example.suportstudy.activity.authencation.LoginAndRegisterMainActivity
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.UserAPI
import com.example.suportstudy.until.Until
import kotlinx.android.synthetic.main.activity_register.edtEmail
import kotlinx.android.synthetic.main.activity_register.edtPassword
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Response

class RegisterFragment : Fragment() {
    var isTutor = false
    var sd: SweetAlertDialog? = null
    var users:Users?=null
    var isLogin = false
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_register, container, false)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val txtHome = view.findViewById<TextView>(R.id.txtHome)

        if (LoginAndRegisterMainActivity.isTutor == true) {
            isTutor = true
        } else {
            isTutor = false
        }
        val userAPI = Until.createRetrofit(UserAPI::class.java)
        sharedPreferences = context!!.getSharedPreferences(
            Until.SHARED_REF_NAME,
            Context.MODE_PRIVATE
        )
        Until.showToast(activity!!, isTutor.toString())
        sd = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        sd!!.titleText = "Đang tạo tài khoản..."
        sd!!.setCancelable(false)

//        var myintent=activity!!.intent
//        isTutor=myintent.getBooleanExtra("isTutor", false)



        txtHome.setOnClickListener {
            LoginAndRegisterMainActivity.isTutor = false
            isTutor = false
            Until.nextActivity(activity!!, MainActivity::class.java)
        }

        btnRegister.setOnClickListener {
            sd!!.show()
            var email = edtEmail.text.toString()
            var name = edtName.text.toString()
            var password = edtPassword.text.toString()
            var call = userAPI.register(
                name,
                email,
                password,
                "https://www.pngitem.com/pimgs/m/130-1300253_female-user-icon-png-download-user-image-color.png",
                isTutor
            )
            sd!!.titleText = "Đang đăng nhập vào ứng dụng..."
            call.enqueue(object : retrofit2.Callback<Users> {
                override fun onResponse(
                    call: retrofit2.Call<Users>,
                    response: Response<Users>
                ) {
                    if (response.isSuccessful) {
                        Log.d("respond", response.body().toString())
                        users = response.body()!!
                        var _id = users!!._id
                        var name = users!!.name
                        var email = users!!.name
                        var image = users!!.image
                        var password = users!!.password

                        isLogin=true
                        val editor = sharedPreferences!!.edit()
                        editor.putString(Until.KEY_ID, _id)
                        editor.putString(Until.KEY_EMAIL, email)
                        editor.putBoolean(Until.KEY_LOGIN, isLogin)
                        editor.putBoolean(Until.KEY_ISTUTOR, isTutor)
                        editor.apply()
                        sd!!.dismiss()
                        Until.nextActivity(
                            activity!!,
                            ListCourseActivity::class.java
                        )
                        activity!!.finish()
                    }
                }

                override fun onFailure(call: retrofit2.Call<Users>, t: Throwable) {
                    sd!!.dismiss()
                    Log.v("Data", "Error: " + t.message.toString())
                }
            })
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}