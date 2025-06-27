package com.app.androidcompose.support.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.androidcompose.R

object AppNotificationManager {

    fun createDefaultNotificationChannel(context: Context) {
        createNotificationChannel(
            context = context,
            channelId = CHANNEL_ID,
            name = context.getString(R.string.app_name),
            description = null,
            importance = NotificationManager.IMPORTANCE_DEFAULT
        )
    }

    fun sendNotification(
        context: Context,
        title: String,
        message: String,
        intent: Intent,
        notificationId: Int = -1,
    ) {
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(notificationId, builder.build())
        }
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    @Suppress("NestedBlockDepth", "LongParameterList")
    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        name: String,
        description: String?,
        importance: Int,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, name, importance).apply {
                description?.let { this.description = description }
                enableVibration(false)
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private const val CHANNEL_ID = "Compose.notification"
}
