package leegroup.module.alarm.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import leegroup.module.alarm.BuildConfig
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.support.extensions.alarmEntryPoint
import leegroup.module.alarm.support.extensions.isAppInForeground
import leegroup.module.alarm.support.helpers.AlarmNotificationHelper
import leegroup.module.alarm.support.helpers.AlarmSoundAndVibrateHelper
import leegroup.module.alarm.ui.screens.quiz.AlarmQuizActivity
import leegroup.module.alarm.worker.AlarmCleanupWorker
import leegroup.module.core.extensions.getParcelable
import leegroup.module.core.extensions.putParcelable
import leegroup.module.core.util.AppConfigProvider

class AlarmService : Service() {

    companion object {
        const val ACTION_START_ALARM = "${BuildConfig.LIBRARY_PACKAGE_NAME}.START_ALARM"
        const val ACTION_STOP_ALARM = "${BuildConfig.LIBRARY_PACKAGE_NAME}.STOP_ALARM"
        const val ACTION_STOP_ALL_ALARM = "${BuildConfig.LIBRARY_PACKAGE_NAME}.STOP_ALL_ALARM"

        fun startService(context: Context, alarm: AlarmModel) {
            val intent = Intent(context, AlarmService::class.java).apply {
                this.action = ACTION_START_ALARM
                putParcelable(alarm)
            }
            context.startForegroundService(intent)
        }

        fun stopService(context: Context, alarm: AlarmModel) {
            val intent = Intent(context, AlarmService::class.java).apply {
                this.action = ACTION_STOP_ALARM
                putParcelable(alarm)
            }
            context.startService(intent)
        }

        fun stopAllService(context: Context) {
            val intent = Intent(context, AlarmService::class.java).apply {
                this.action = ACTION_STOP_ALL_ALARM
            }
            context.startService(intent)
        }
    }

    // 3 main services
    private lateinit var notificationHelper: AlarmNotificationHelper
    private lateinit var alarmSoundAndVibrateHelper: AlarmSoundAndVibrateHelper
    private lateinit var appConfigProvider: AppConfigProvider
    private var countdownJob: Job? = null
    private var activeAlarmId: Int = 0

    override fun onCreate() {
        super.onCreate()
        notificationHelper = applicationContext.alarmEntryPoint.alarmNotificationHelper()
        alarmSoundAndVibrateHelper = applicationContext.alarmEntryPoint.alarmSoundHelper()
        appConfigProvider = applicationContext.alarmEntryPoint.appConfigProvider()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: ACTION_START_ALARM

        when (action) {
            ACTION_START_ALARM -> handleStartAlarm(intent)
            ACTION_STOP_ALARM -> handleStopAlarm(intent)
            ACTION_STOP_ALL_ALARM -> stopSelf()
            else -> throw IllegalArgumentException("Unknown action: $action")
        }

        return START_STICKY
    }

    private fun handleStopAlarm(intent: Intent?) {
        val alarm = intent?.getParcelable<AlarmModel>() ?: return
        val alarmId = alarm.id
        if (activeAlarmId == alarmId || activeAlarmId <= 0) {
            stopSelf()
        }
    }

    private fun handleStartAlarm(intent: Intent?) {
        val alarm = intent?.getParcelable<AlarmModel>() ?: return

        activeAlarmId = alarm.id

        showNotification(alarm)
        alarmSoundAndVibrateHelper.start(alarm)
        startCountDown(alarm = alarm)
    }

    private fun openQuizScreen(alarm: AlarmModel) {
        if (isAppInForeground && appConfigProvider.currentActivity !is AlarmQuizActivity) {
            val intent = AlarmQuizActivity.getAlarmQuizIntent(this, alarm)
            startActivity(intent)
        }
    }

    private fun showNotification(alarm: AlarmModel) {
        startForeground(
            AlarmNotificationHelper.ALARM_NOTIFICATION_ID,
            notificationHelper.buildActiveAlarmNotification(alarm)
        )
    }

    private fun startCountDown(
        alarm: AlarmModel,
        durationSecs: Int = 300
    ) {
        countdownJob?.cancel()
        countdownJob = CoroutineScope(Dispatchers.Default).launch {
            var remaining = durationSecs
            while (remaining >= 0) {
                // Start the Quiz Screen
                openQuizScreen(alarm)

                delay(1000)
                showNotification(alarm)
                remaining--
            }
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
        alarmSoundAndVibrateHelper.stop()
        countdownJob?.cancel()
        if (activeAlarmId > 0) {
            AlarmCleanupWorker.startWorker(this, activeAlarmId)
        }

        activeAlarmId = 0
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
