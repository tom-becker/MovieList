package com.tombecker.moviesearch.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData

class NetworkChangeReceiver : BroadcastReceiver() {

    val noNetworkLiveData = MutableLiveData<Boolean>()

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            noNetworkLiveData.postValue(isNetworkAvailable(it))
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}