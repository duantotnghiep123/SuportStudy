package com.example.suportstudy.until

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.suportstudy.R
import com.example.suportstudy.model.Question
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.regex.Pattern

object Constrain {

    val appId = "duantotnghiep-aeidb";

    //    var baseUrl="http://192.168.3.107:10000"
    var baseUrl = "http://192.168.1.11:3000"
    var firebaseUrl="https://suportstudy-72e5e-default-rtdb.firebaseio.com/"
//    var baseUrl="http://172.20.10.3:10000"

    var SHARED_REF_NAME: String? = "savestatuslogin"
    var KEY_ID = "_id"
    var KEY_NAME = "name"
    var KEY_IMAGE = "image"
    var KEY_EMAIL = "email"
    var KEY_LOGIN = "islogin"
    var KEY_ISTUTOR = "isTutor"

    val VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

     val STORAGE_REQUEST_CODE = 300
     val IMAGE_PICK_GALLERY_CODE = 400

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


    fun sweetdialog(context: Context, title: String):SweetAlertDialog{
        var sd = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
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
    fun <T> createRetrofit2(clazz: Class<T>): T {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.4:10000")
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


    fun checkShowImage(context: Context,imageUrl:String,imageView: ImageView){
        try {
            if(!imageUrl.equals("")){
                Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_gallery_grey).into(imageView)
            }else{
                imageView!!.setImageResource(R.drawable.ic_gallery_grey)
            }
        }catch (e: Exception){
            imageView!!.setImageResource(R.drawable.ic_gallery_grey)
        }
    }
    fun createDialog(context: Context,layout:Int):Dialog{
        val dialog = Dialog(context)
        dialog.setContentView(layout)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        val window = dialog!!.window
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (dialog != null && dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog!!.setCancelable(false)

        return dialog
    }

}