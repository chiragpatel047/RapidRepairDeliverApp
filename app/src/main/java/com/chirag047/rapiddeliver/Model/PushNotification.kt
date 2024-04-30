package com.chirag047.rapiddeliver.Model

data class PushNotification(
    val data : FirebaseNotificationModel,
    val to : String
)
