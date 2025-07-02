package com.app.androidcompose

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.app.androidcompose.support.notification.AppNotificationManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import leegroup.module.alarm.support.helpers.AlarmNotificationHelper
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltAndroidApp
class AppApplication : Application() {

    @Inject
    lateinit var alarmNotificationHelper: AlarmNotificationHelper

    override fun onCreate() {
        super.onCreate()
        AppNotificationManager.createDefaultNotificationChannel(this)
        alarmNotificationHelper.createChannels()
        FirebaseApp.initializeApp(this)
        setupLogging()

        observeActivityLifecycle()
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun observeActivityLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                currentActivity = WeakReference(activity)
            }

            override fun onActivityPaused(activity: Activity) {
                if (currentActivity.get() == activity) {
                    currentActivity.clear()
                }
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
            override fun onActivityStarted(p0: Activity) {}
            override fun onActivityStopped(p0: Activity) {}
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
            override fun onActivityDestroyed(p0: Activity) {}
        })
    }

    companion object {
        var currentActivity: WeakReference<Activity> = WeakReference(null)
    }
}