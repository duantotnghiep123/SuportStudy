package com.example.suportstudy.until

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.model.Question
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

object Constrain {

    val appId = "duantotnghiep-aeidb";

    //    var baseUrl="http://192.168.3.107:10000"
    var baseUrl = "http://192.168.1.6:3000"
//    var baseUrl="http://172.20.10.3:10000"

    var SHARED_REF_NAME: String? = "savestatuslogin"
    var KEY_ID = "_id"
    var KEY_NAME = "name"
    var KEY_EMAIL = "email"
    var KEY_LOGIN = "islogin"
    var KEY_ISTUTOR = "isTutor"

    val VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    fun <T> nextActivity(context: Context, clazz: Class<T>) {
        var intent = Intent(context, clazz);
        context.startActivity(intent)
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun hideKeyBoard(context: Activity) {
        @SuppressLint("ServiceCast") val inputManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(context.currentFocus!!.windowToken, 0)
    }

    fun searchView(
        searchView: androidx.appcompat.widget.SearchView,
        msg: String
    ): androidx.appcompat.widget.SearchView {
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
    fun sweetdialog(context: Context, title: String):SweetAlertDialog{
        var sd: SweetAlertDialog? = null
        sd = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        sd!!.titleText = title
        sd!!.setCancelable(false)

        return sd
    }
    var retrofit: Retrofit? = null
    fun <T> createRetrofit(clazz: Class<T>): T {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(clazz);
    }

    fun showQuestion(
        op1: RadioButton,
        op2: RadioButton,
        op3: RadioButton,
        op4: RadioButton,
        list: List<Question>,
        index: Int
    ) {
        op1.text = list.get(index).option1
        op2.text = list.get(index).option2
        op3.text = list.get(index).option3
        op4.text = list.get(index).option4
    }



}