package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.activity.course.ListCourseActivity
import com.example.suportstudy.model.Users
import com.example.suportstudy.service.UserAPI
import com.example.suportstudy.until.Until
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response

class LoginFragment : Fragment() {
    @SuppressLint("UseRequireInsteadOfGet")
    var sd:SweetAlertDialog?=null
    var listUser: List<Users>?=null
    var  checkLogin=false

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
        var view = inflater.inflate(R.layout.fragment_login, container, false)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        sd=Until.sweetdialog(activity!!,"Đang đăng nhập")

        btnLogin.setOnClickListener {
            sd!!.show()
            var email = edtEmail.text.toString()
            var password = edtPassword.text.toString()
            if(email.equals("")){
                Until.showToast(activity!!,"Vui lòng nhập email")
                sd!!.dismiss()

            }else if(password.equals("")){
                Until.showToast(activity!!,"Vui lòng nhập mật khẩu")
                sd!!.dismiss()

            }else{
               loginFuntion(email,password)
            }
        }

        return view
    }

    fun loginFuntion(email:String,password:String){
        val userAPI = Until.createRetrofit(UserAPI::class.java)
        var call = userAPI.getAllUsers()
        call.enqueue(object : retrofit2.Callback<List<Users>> {
            override fun onResponse(
                call: retrofit2.Call<List<Users>>,
                response: Response<List<Users>>
            ) {
                if (response.code() == 200) {
                    listUser = response.body()
                    for (i in listUser!!.indices) {
                        var   _id=listUser!![i]._id
                        var   userEmail=listUser!![i].email
                        var  userPassword=listUser!![i].password
                        var  istutor=listUser!![i].isTutor
                        if (userEmail.equals(email) && userPassword.equals(password)
                        ) {
                            checkLogin=true
                            Log.d("email",_id+ email)
                            isLogin=true
                            val editor = sharedPreferences!!.edit()
                            editor.putString(Until.KEY_ID, _id)
                            editor.putString(Until.KEY_EMAIL, userEmail)
                            editor.putBoolean(Until.KEY_LOGIN, isLogin)
                            editor.putBoolean(Until.KEY_ISTUTOR, istutor)
                            editor.apply()
                            break
                        }
                    }
                    if(checkLogin==true){
                        Until.nextActivity(activity!!,ListCourseActivity::class.java)
                        activity!!.finish()
                    }else{
                        Until.showToast(activity!!,"Email hoặc mật khẩu không đúng")

                    }
                    sd!!.dismiss()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Users>>, t: Throwable) {
                sd!!.dismiss()
                Log.v("Data", "Error:" + t.message.toString())
            }
        })
        sd!!.dismiss()

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}



