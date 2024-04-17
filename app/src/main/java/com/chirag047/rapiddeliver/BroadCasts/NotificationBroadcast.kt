package com.chirag047.rapiddeliver.BroadCasts


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.chirag047.rapiddeliver.Services.LocationService

class NotificationBroadcast() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.getIntExtra("EXTRA_NOTIFICATION_ID", 0)!!.equals(20)) {
            try {
                var service: Intent? = Intent(context, LocationService::class.java)
                context?.stopService(service)
            } catch (e: Exception) {
                Log.d("StopServiceLogTag", e.message.toString())
            }
        }
    }
}