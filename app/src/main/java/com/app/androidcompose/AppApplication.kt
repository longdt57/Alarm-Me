package com.app.androidcompose

import android.app.Application
import com.app.androidcompose.support.notification.AppNotificationManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppNotificationManager.createDefaultNotificationChannel(this)
        FirebaseApp.initializeApp(this)
        setupLogging()
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}