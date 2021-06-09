package com.example.suportstudy.authencation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.suportstudy.R
import com.example.suportstudy.chat.ChatActivity
import com.example.suportstudy.home.HomeActivity
import com.example.suportstudy.until.Until
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edtEmail
import kotlinx.android.synthetic.main.activity_login.edtPassword


class LoginActivity : AppCompatActivity() {
    val context=this@LoginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Realm.init(applicationContext)
        val app = App(AppConfiguration.Builder(Until.appId).build())

        btnLogin.setOnClickListener {
            var email=edtEmail.text.toString()
            var password=edtPassword.text.toString()
            var credentials= Credentials.emailPassword(email, password)
            var user: User?
            app.loginAsync(credentials) {
                if (it.isSuccess) {
                    Until.showToast(context,"Đăng nhập thành công");
                    user = app.currentUser()
                    Until.nextActivity(context,HomeActivity::class.java)
                    Log.d("User",user!!.id)

                } else {
                    Until.showToast(context,"Đăng nhập thất bại");
                }
            }
        }
        btnRegister.setOnClickListener {
            Until.nextActivity(context,RegisterActivity::class.java)
        }
    }
}