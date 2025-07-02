package leegroup.module.alarm.support.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import leegroup.module.alarm.R
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.support.extensions.notificationManager
import leegroup.module.alarm.ui.screens.quiz.AlarmQuizActivity
import leegroup.module.core.extensions.putParcelable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmNotificationHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun createChannels() {
        createAlarmNotificationChannel(
            channelId = ALARM_NOTIFICATION_CHANNEL_ID,
            importance = NotificationManager.IMPORTANCE_HIGH,
            bypassDnd = true
        )
    }

    private fun createAlarmNotificationChannel(
        channelId: String = ALARM_NOTIFICATION_CHANNEL_ID,
        name: String = context.getString(R.string.alarm),
        importance: Int,
        bypassDnd: Boolean = false
    ) {
        val channel = NotificationChannel(
            channelId,
            name,
            importance
        ).apply {
            setBypassDnd(bypassDnd)
            setSound(null, null)
        }

        context.notificationManager.createNotificationChannel(channel)
    }

    fun buildActiveAlarmNotification(alarm: AlarmModel): Notification {
        val contentTitle = alarm.label

        val reminderIntent =
            AlarmQuizActivity.getAlarmQuizIntent(context, alarm, shouldOpenAppWhenFinish = true)

        val pendingIntent = PendingIntent.getActivity(
            context, alarm.id, reminderIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, ALARM_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(contentTitle)
            .setContentText(alarm.displayTime)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(pendingIntent, true)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()
    }

    companion object {
        private const val ALARM_NOTIFICATION_CHANNEL_ID = "alarm_notification_channel_id"

        const val ALARM_NOTIFICATION_ID = 9998
    }
}
