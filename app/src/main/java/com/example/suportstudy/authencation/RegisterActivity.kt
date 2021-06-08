package com.example.suportstudy.authencation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.home.HomeActivity
import com.example.suportstudy.until.Until
import io.realm.Realm
import io.realm.mongodb.*
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoDatabase
import io.realm.mongodb.mongo.iterable.FindIterable
import kotlinx.android.synthetic.main.activity_register.*
import org.bson.Document
import org.json.JSONObject
import io.realm.mongodb.mongo.MongoCollection as MongoCollection1


class RegisterActivity : AppCompatActivity() {


    var mongoClient: MongoClient? = null
    var mongoDatabase: MongoDatabase? = null
    var mongoCollection: MongoCollection1<Document>? = null
    var user:User?=null
    var isTutor=false
    var sd: SweetAlertDialog? = null
    val  context=this@RegisterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sd = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sd!!.titleText ="Đang tạo tài khoản..."
        sd!!.setCancelable(false)

        var myintent=intent
        isTutor=myintent.getBooleanExtra("isTutor", false)

        Realm.init(applicationContext)
        val app = App(AppConfiguration.Builder(Until.appId).build())

        btnRegister.setOnClickListener {
            sd!!.show()
            var email=edtEmail.text.toString()
            var password=edtPassword.text.toString()

            app.emailPassword.registerUserAsync(email, password) { r->
                if (r.isSuccess) {
//                    Until.showToast(applicationContext, getString(R.string.dangkythanhcong))
                    var credentials= Credentials.emailPassword(email, password)
                    sd!!.titleText ="Đang đăng nhập vào ứng dụng..."
                    app.loginAsync(credentials) {
                        if (it.isSuccess) {
//                            Until.showToast(applicationContext, "Đăng nhập thành công");
                            user = app.currentUser()!!
                            mongoClient = user!!.getMongoClient("mongodb-atlas")
                            mongoDatabase = mongoClient!!.getDatabase("SuportStudy")
                            mongoCollection = mongoDatabase!!.getCollection("users")


                            mongoCollection!!.insertOne(
                                Document(
                                    "uid", user!!.id
                                )
                                    .append("name", "Nguyễn Văn Quế")
                                    .append("email", email)
                                    .append("password", password)
                                    .append("isTutor", isTutor)
                            ).getAsync { rsul->
                                if (rsul.isSuccess) {
                                    sd!!.dismiss()
                                    Log.v("Data", "Data Inserted Successfully")
                                     Until.nextActivity(
                                         context,
                                         HomeActivity::class.java
                                     )
                                     finish()
                                } else {
                                    sd!!.dismiss()
                                    Log.v("Data", "Error:" + rsul.error.toString())
                                }
                            }
                        } else {
                            sd!!.dismiss()
                            Until.showToast(context, "Đăng nhập thất bại");
                        }
                    }


                } else {
                    sd!!.dismiss()
                    Until.showToast(context, "Lỗi ${r.error}")
                }
            }
        }
        btnBack.setOnClickListener {
//            user?.logOutAsync {
//                if (it.isSuccess) {
//                    Log.v("AUTH", "Successfully logged out.")
//                } else {
//                    Log.e("AUTH", it.error.toString())
//                }
//            }
           Until.nextActivity(applicationContext, LoginActivity::class.java)
        }
    }



}


