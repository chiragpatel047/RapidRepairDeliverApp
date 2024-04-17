package com.chirag047.rapiddeliver.Services


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.chirag047.rapiddeliver.BroadCasts.NotificationBroadcast
import com.chirag047.rapiddeliver.Model.Coordinates
import com.chirag047.rapiddeliver.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.common.util.concurrent.ServiceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LocationService() : Service() {

    companion object {
        const val CHANNEL_ID = "LOCATIONID"
        const val NOTIFICATION_ID = 20
    }

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var locationRequest: LocationRequest? = null

    private var notificationManager: NotificationManager? = null

    private var location: Location? = null

    private val firestore: FirebaseFirestore = Firebase.firestore

    var orderId: String = ""

    override fun onCreate() {
        super.onCreate()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 7000)
                .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
            }

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult)
            }
        }

        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "LocationChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    @SuppressLint("MissingPermission")
    fun createLocationRequest() {

        try {
            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest!!,
                locationCallback!!,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeLocationRequest() {
        locationCallback?.let {
            fusedLocationProviderClient?.removeLocationUpdates(it)
        }

        Log.d("StopServiceLogTag", "ServiceStopped")

        stopForeground(true)
        stopSelf()

    }

    @SuppressLint("ForegroundServiceType")
    private fun onNewLocation(locationResult: LocationResult) {

        location = locationResult.lastLocation

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                getNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(NOTIFICATION_ID, getNotification())
        }

        CoroutineScope(Dispatchers.IO).launch {


            firestore.collection("liveTrack")
                .document(orderId!!)
                .set(Coordinates(location!!.latitude, location!!.longitude))
                .await()

            Log.d(
                "FirebaseWriteRequest",
                location!!.latitude.toString() + "  " + location!!.longitude.toString()
            )
        }
    }

    private fun getNotification(): Notification {

        val broadcastIntent = Intent(
            this,
            NotificationBroadcast::class.java
        )

        broadcastIntent.setAction("Stop Tracking")
        broadcastIntent.putExtra("EXTRA_NOTIFICATION_ID", NOTIFICATION_ID)
        val snoozePendingIntent = PendingIntent.getBroadcast(
            this, 0, broadcastIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
        notification.setContentTitle("Location Tracking")
            .setContentTitle("Latitude : ${location?.latitude} Longitude : ${location?.longitude}")
            .setOngoing(true)
            .setSilent(true)
            .setSmallIcon(R.mipmap.ic_launcher)

        return notification.build()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        orderId = intent!!.getStringExtra("orderId")!!
        createLocationRequest()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        removeLocationRequest()
    }
}