package com.example.suportstudy.until

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.example.suportstudy.R
import com.example.suportstudy.model.Question
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_quizz.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Until {

    val appId="duantotnghiep-aeidb";
//    var baseUrl="http://192.168.3.107:10000"
    var baseUrl="http://192.168.1.7:10000"
//    var baseUrl="http://172.20.10.3:10000"

//    val app = App(AppConfiguration.Builder(appId).build())
//    var getCurrentUser: User? =app.currentUser()

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
     fun hideKeyBoard(context: Activity) {
        @SuppressLint("ServiceCast") val inputManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(context.currentFocus!!.windowToken, 0)
    }

    fun searchView(searchView: androidx.appcompat.widget.SearchView, msg:String):androidx.appcompat.widget.SearchView{
        searchView.setActivated(true)
        searchView.setQueryHint(Html.fromHtml("<font color = #ACACAC>" + msg + "</font>"))
        searchView.onActionViewExpanded()
        searchView.setIconified(false)
        searchView.clearFocus()
        searchView.setFocusable(false)
        val linearLayout1 = searchView.getChildAt(0) as LinearLayout
        val linearLayout2 = linearLayout1.getChildAt(2) as LinearLayout
        val linearLayout3 = linearLayout2.getChildAt(1) as LinearLayout
        val autoComplete = linearLayout3.getChildAt(0) as AutoCompleteTextView
        autoComplete.textSize = 15f
        return searchView
    }
    var retrofit: Retrofit?=null
    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

   fun showQuestion(op1: RadioButton,op2: RadioButton,op3: RadioButton,op4: RadioButton,list: List<Question>,index:Int){
       op1.text = list.get(index).option1
       op2.text = list.get(index).option2
       op3.text = list.get(index).option3
       op4.text = list.get(index).option4
   }

}