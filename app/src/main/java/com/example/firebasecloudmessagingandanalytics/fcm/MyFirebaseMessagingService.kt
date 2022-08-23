package com.example.firebasecloudmessagingandanalytics.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.firebasecloudmessagingandanalytics.R
import com.example.firebasecloudmessagingandanalytics.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService(){
    private val channelName = "Notifications"
    private val notificationId = 101
    private val channelId = channelName + notificationId
    private val description = "for important updates"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("noti", message.notification?.title.toString())
        Log.d(TAG, message.notification?.title.toString())
        if(message.notification != null){
            showNotification(message.notification?.title.toString(), message.notification?.body.toString())
        }
    }

    private fun showNotification(title: String, body: String){

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_noty_background)

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId).apply {
            setSmallIcon(R.drawable.ic_notification)
            setContentTitle(title)
            setContentText(body)
            setLargeIcon(largeIcon)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            setDefaults(Notification.DEFAULT_ALL)
            priority = Notification.PRIORITY_HIGH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var mChannel: NotificationChannel? = notificationManager.getNotificationChannel(channelId)
            if (mChannel == null) {
                mChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                mChannel.description = description
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(1000, 1000)
                notificationManager.createNotificationChannel(mChannel)
            }
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object{
        private const val TAG = "FCM"
    }
}