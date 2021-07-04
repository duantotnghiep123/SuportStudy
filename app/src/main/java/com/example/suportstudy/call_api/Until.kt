package com.example.suportstudy.call_api

import android.R
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast

object Until {

    fun reportMessage(context: Context?, message: String?) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        val v = toast.view!!.findViewById<View>(R.id.message) as TextView
        if (v != null) v.gravity = Gravity.CENTER
        toast.show()
    }

    fun postDelay(runnable: Runnable?, delayMillis: Long) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable!!, delayMillis)
    }

    fun runOnUithread(runnable: Runnable?) {
        val handler = Handler(Looper.getMainLooper())
        handler.post(runnable!!)
    }
}