package com.chirag047.rapiddeliver.Services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.chirag047.rapiddeliver.Model.NotificationModel
import com.chirag047.rapiddeliver.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

const val CHANNEL_ID = "ORDER"

class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // val intent = Intent(this,Notifcations::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

//        val pendingIntent = PendingIntent.getActivity(this,0,intent,
//            FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)
//
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.mipmap.rd_image_logo)
            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)

        val dateFormate = SimpleDateFormat("dd MMMM yyyy")
        val currentDate = dateFormate.format(Date())

        val timeFormate = SimpleDateFormat("hh:mm a")
        val currentTime = timeFormate.format(Date())


        val notificationModel = NotificationModel(
            System.currentTimeMillis().toString(),
            message.data["message"]!!,
            currentDate,
            currentTime
        )

        val firestore = Firebase.firestore
        val auth = Firebase.auth

        firestore.collection("mechanicUsers")
            .document(auth.currentUser!!.uid)
            .collection("notifications")
            .document(notificationModel.notificationId)
            .set(notificationModel).addOnCompleteListener {

            }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "ORDER_CHANNEL"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "ORDER_CHANNEL_DESCRIPTION"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}