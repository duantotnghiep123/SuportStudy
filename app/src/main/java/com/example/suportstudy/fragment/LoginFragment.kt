package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.activity.authencation.RegisterActivity
import com.example.suportstudy.activity.chat.ChatActivity
import com.example.suportstudy.activity.home.HomeActivity
import com.example.suportstudy.until.Until
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginFragment : Fragment() {
    var sd: SweetAlertDialog? = null
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
        var view= inflater.inflate(R.layout.fragment_login, container, false)
        val btnLogin=view.findViewById<Button>(R.id.btnLogin)
        Realm.init(activity)
        val app = App(AppConfiguration.Builder(Until.appId).build())

        sd = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        sd!!.titleText ="Đang đăng nhập..."
        sd!!.setCancelable(false)

        btnLogin.setOnClickListener {
            sd!!.show()
            var email=edtEmail.text.toString()
            var password=edtPassword.text.toString()
            var credentials= Credentials.emailPassword(email, password)

            app.loginAsync(credentials) {
                if (it.isSuccess) {
                    Until.nextActivity(activity!!, HomeActivity::class.java)
                } else {
                    Until.showToast(activity!!,"Đăng nhập thất bại");
                }
                sd!!.dismiss()
            }
        }

        return view
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