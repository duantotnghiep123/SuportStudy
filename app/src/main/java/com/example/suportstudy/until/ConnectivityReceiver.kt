package com.example.suportstudy.until

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.provider.Settings
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.suportstudy.R
import com.example.suportstudy.activity.MainActivity
import com.google.android.material.snackbar.Snackbar

class ConnectivityReceiver(var homeLayaout: View) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {

        } else {

            val snackbar: Snackbar = Snackbar
                .make(homeLayaout, R.string.No_internet_connection, Snackbar.LENGTH_LONG)
                .setAction("Mở cài đặt", {
                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    context.startActivity(intent)
                })
            // Changing action button text color
            snackbar.setActionTextColor(context.getResources().getColor(R.color.red))
            // Changing message text color
            val view = snackbar.view
            view.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.teal_200
                )
            )
            val textView = view.findViewById<TextView>(R.id.snackbar_text)
            textView.setTextColor(Color.WHITE)
            snackbar.show()
        }
    }
}