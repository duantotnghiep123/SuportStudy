package com.example.suportstudy.until

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoCollection
import io.realm.mongodb.mongo.MongoDatabase
import org.bson.Document

object Until {

    val appId="duantotnghiep-aeidb";
//    var baseQuestionUrl="http://192.168.3.107:10000"
    var baseQuestionUrl="http://172.20.10.3:10000"

    val app = App(AppConfiguration.Builder(appId).build())
    var getCurrentUser: User? =app.currentUser()


    fun <T> nextActivity(context: Context, clazz: Class<T>) {
        var intent = Intent(context, clazz);
        context.startActivity(intent)
    }

    fun showToast(context: Context,msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
    }
    fun app():App{
        val app = App(AppConfiguration.Builder(appId).build())
        return app
    }


    fun initMongoCollection(dbName:String,tableName:String):MongoCollection<Document>{
        var mongoClient: MongoClient= getCurrentUser!!.getMongoClient("mongodb-atlas")
        var mongoDatabase: MongoDatabase=mongoClient!!.getDatabase(dbName)
        var mongoCollection: MongoCollection<Document> =mongoDatabase!!.getCollection(tableName)
        return mongoCollection
    }
     fun hideKeyBoard(context: Activity) {
        @SuppressLint("ServiceCast") val inputManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(context.currentFocus!!.windowToken, 0)
    }


}