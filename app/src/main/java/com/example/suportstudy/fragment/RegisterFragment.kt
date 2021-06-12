package com.example.suportstudy.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.activity.MainActivity
import com.example.suportstudy.activity.authencation.LoginActivity
import com.example.suportstudy.activity.authencation.LoginAndRegisterMainActivity
import com.example.suportstudy.activity.home.HomeActivity
import com.example.suportstudy.until.Until
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoCollection
import io.realm.mongodb.mongo.MongoDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btnRegister
import kotlinx.android.synthetic.main.activity_register.edtEmail
import kotlinx.android.synthetic.main.activity_register.edtPassword
import kotlinx.android.synthetic.main.fragment_register.*
import org.bson.Document

class RegisterFragment : Fragment() {

    var mongoClient: MongoClient? = null
    var mongoDatabase: MongoDatabase? = null
    var mongoCollection: MongoCollection<Document>? = null
    var user: User?=null
    var isTutor=false
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
      var  view= inflater.inflate(R.layout.fragment_register, container, false)
        val  btnRegister=view.findViewById<Button>(R.id.btnRegister)
        val  txtHome=view.findViewById<TextView>(R.id.txtHome)

        if(LoginAndRegisterMainActivity.isTutor==true){
            isTutor=true
        }else{
            isTutor=false
        }
        Until.showToast(activity!!,isTutor.toString())
        sd = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        sd!!.titleText ="Đang tạo tài khoản..."
        sd!!.setCancelable(false)

//        var myintent=activity!!.intent
//        isTutor=myintent.getBooleanExtra("isTutor", false)

        Realm.init(activity)
        val app = App(AppConfiguration.Builder(Until.appId).build())

        txtHome.setOnClickListener {
            LoginAndRegisterMainActivity.isTutor=false
            isTutor=false
            Until.nextActivity(activity!!,MainActivity::class.java)
        }

        btnRegister.setOnClickListener {
            sd!!.show()
            var email=edtEmail.text.toString()
            var name=edtName.text.toString()
            var password=edtPassword.text.toString()

            app.emailPassword.registerUserAsync(email, password) { r->
                if (r.isSuccess) {
                    var credentials= Credentials.emailPassword(email, password)
                    sd!!.titleText ="Đang đăng nhập vào ứng dụng..."
                    app.loginAsync(credentials) {
                        if (it.isSuccess) {
                            user = app.currentUser()!!
                            mongoClient = user!!.getMongoClient("mongodb-atlas")
                            mongoDatabase = mongoClient!!.getDatabase("SuportStudy")
                            mongoCollection = mongoDatabase!!.getCollection("users")
                            mongoCollection!!.insertOne(
                                Document(
                                    "uid", user!!.id
                                )
                                    .append("name",name )
                                    .append("email", email)
                                    .append("password", password)
                                    .append("isTutor", isTutor)
                            ).getAsync { rsul->
                                if (rsul.isSuccess) {
                                    sd!!.dismiss()
                                    Log.v("Data", "Data Inserted Successfully")
                                    Until.nextActivity(
                                        activity!!,
                                        HomeActivity::class.java
                                    )
                                    activity!!.finish()
                                } else {
                                    sd!!.dismiss()
                                    Log.v("Data", "Error:" + rsul.error.toString())
                                }
                            }
                        } else {
                            sd!!.dismiss()
                            Until.showToast(activity!!, "Đăng nhập thất bại");
                        }
                    }
                } else {
                    sd!!.dismiss()
                    Until.showToast(activity!!, "Lỗi ${r.error}")
                }
            }
        }
//        btnBack.setOnClickListener {
////            user?.logOutAsync {
////                if (it.isSuccess) {
////                    Log.v("AUTH", "Successfully logged out.")
////                } else {
////                    Log.e("AUTH", it.error.toString())
////                }
////            }
//            Until.nextActivity(activity!!, LoginActivity::class.java)
//        }
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