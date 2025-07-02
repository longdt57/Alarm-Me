package leegroup.module.alarm.support.extensions

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.EntryPointAccessors
import leegroup.module.alarm.BuildConfig
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.di.AlarmEntryPoint
import leegroup.module.alarm.receiver.AlarmReceiver
import leegroup.module.alarm.service.AlarmService
import leegroup.module.core.extensions.orFalse
import leegroup.module.core.extensions.putParcelable
import java.util.Calendar

const val ALARM_ID = "alarm_id"
internal const val OPEN_ALARMS_TAB_INTENT_ID = 1994

internal val Context.notificationManager: NotificationManager get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
internal val Context.alarmManager: AlarmManager get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager
internal val Context.audioManager: AudioManager get() = getSystemService(Context.AUDIO_SERVICE) as AudioManager
internal val Context.vibrator: Vibrator get() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
internal fun isUpsideDownCakePlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
internal fun isOreoPlus() = true

internal val Context.alarmEntryPoint: AlarmEntryPoint
    get() = EntryPointAccessors
        .fromApplication(this, AlarmEntryPoint::class.java)

internal fun Context.setupAlarmClock(alarm: AlarmModel) {
    val triggerTimeMillis = getTimeOfNextAlarm(alarm)?.timeInMillis ?: return
    val alarmManager = alarmManager
    try {
        AlarmManagerCompat.setAlarmClock(
            alarmManager,
            triggerTimeMillis,
            getOpenAlarmTabIntent(),
            getAlarmPendingIntent(alarm)
        )
        Log.d(
            "AlarmSetup",
            "Success to set alarm clock for ${alarm}:, trigger in $triggerTimeMillis"
        )
        if (BuildConfig.DEBUG) {
            Toast.makeText(
                this,
                "Alarm is triggered in: ${(triggerTimeMillis - System.currentTimeMillis()) / 1000} seconds",
                Toast.LENGTH_SHORT
            ).show()
        }
    } catch (e: Exception) {
        Log.e("AlarmSetup", "Failed to set alarm clock for ${alarm}: ${e.message}")
    }
}

internal fun Context.cancelAlarm(alarm: AlarmModel) {
    alarmManager.cancel(getAlarmPendingIntent(alarm))
    stopAlarmService(alarm)
}

internal fun getTimeOfNextAlarm(alarm: AlarmModel): Calendar? {
    if (alarm.isEnabled.orFalse.not()) return null

    val alarmTimeInMinutes = alarm.timeInMinutes
    val repeatDays = alarm.repeatDays.orEmpty()

    val nextAlarmTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, alarmTimeInMinutes / 60)
        set(Calendar.MINUTE, alarmTimeInMinutes % 60)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return when {
        alarm.isRecurring().not() -> {
            if (alarm.isToday()) {
                nextAlarmTime
            } else if (alarm.isTomorrow()) {
                nextAlarmTime.apply { add(Calendar.DAY_OF_MONTH, 1) }
            } else null
        }

        else -> {
            val now = Calendar.getInstance()
            repeat(8) {
                val dayOfWeek =
                    nextAlarmTime.get(Calendar.DAY_OF_WEEK) // Sunday = 1 ... Saturday = 7
                if (repeatDays.contains(dayOfWeek) && now < nextAlarmTime) {
                    return nextAlarmTime
                } else {
                    nextAlarmTime.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            null
        }
    }
}

internal fun Context.getOpenAlarmTabIntent(): PendingIntent {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    val componentName = intent?.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
//    intent?.putExtra(OPEN_TAB, TAB_ALARM)
    return PendingIntent.getActivity(
        this,
        OPEN_ALARMS_TAB_INTENT_ID,
        mainIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

internal fun Context.getAlarmPendingIntent(alarm: AlarmModel): PendingIntent {
    val intent = Intent(this, AlarmReceiver::class.java)
    intent.putParcelable(alarm)
    return PendingIntent.getBroadcast(
        this,
        alarm.id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

fun Context.stopAlarmService(alarm: AlarmModel) {
    AlarmService.stopService(this, alarm)
}

fun Context.stopAllAlarmService() {
    AlarmService.stopAllService(this)
}

fun Context.startAlarmService(alarm: AlarmModel) {
    AlarmService.startService(this, alarm)
}

internal fun Context.canUseFullScreenIntent(): Boolean {
    return !isUpsideDownCakePlus() || notificationManager.canUseFullScreenIntent()
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
internal fun Context.openFullScreenIntentSettings(appId: String) {
    if (isUpsideDownCakePlus()) {
        val uri = Uri.fromParts("package", appId, null)
        val intent = Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT)
        intent.data = uri
        startActivity(intent)
    }
}

internal fun Context.openNotificationSettings() {
    if (isOreoPlus()) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        startActivity(intent)
    } else {
        // For Android versions below Oreo, you can't directly open the app's notification settings.
        // You can open the general notification settings instead.
        val intent = Intent(Settings.ACTION_SETTINGS)
        startActivity(intent)
    }
}

internal val isAppInForeground: Boolean
    get() = ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

