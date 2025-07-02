package leegroup.module.core.util

import android.app.Activity
import android.content.Context

interface AppConfigProvider {
    val isDebug: Boolean
    val currentActivity: Activity?

    fun restartApp(context: Context)
}