package com.example.suportstudy.until

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData

class ConnectionManager(val context: Context):LiveData<Boolean> (){

    private var connectivityManager:ConnectivityManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
     lateinit var networkCallback:ConnectivityManager.NetworkCallback
    override fun onActive() {
        super.onActive()
        updateConnection()
        when{
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.N->{
                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
            }
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP->{
                lolipopnetworkRequest()
            }
            else ->{
                context.registerReceiver(
                    networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    override fun onInactive() {
        super.onInactive()

        try {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                connectivityManager.unregisterNetworkCallback(connectivityManagerCallback())
            }else{
                context.unregisterReceiver(networkReceiver)
            }
        }catch (e:Exception){

        }

    }
     @TargetApi(Build.VERSION_CODES.LOLLIPOP)
     private fun lolipopnetworkRequest(){
          var requestBuilder=NetworkRequest.Builder()
              .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
              .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
              .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

         connectivityManager.registerNetworkCallback(
             requestBuilder.build(),
             connectivityManagerCallback()
         )
     }
    private fun connectivityManagerCallback():ConnectivityManager.NetworkCallback{
         if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
             networkCallback=object :ConnectivityManager.NetworkCallback(){
                 override fun onLost(network: Network) {
                     super.onLost(network)
                     postValue(false)
                 }

                 override fun onAvailable(network: Network) {
                     super.onAvailable(network)
                     postValue(true)
                 }
             }
             return networkCallback
         }else{
             throw IllegalAccessError("Error")
         }
     }

    private val networkReceiver=object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnection()
        }

    }

    fun updateConnection(){
        var activeNetwork:NetworkInfo?=connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected==true)
    }


}