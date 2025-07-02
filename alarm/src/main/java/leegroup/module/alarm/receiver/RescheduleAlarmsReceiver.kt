package leegroup.module.alarm.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import leegroup.module.alarm.worker.RescheduleAlarmsWorker

class RescheduleAlarmsReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        RescheduleAlarmsWorker.startWorker(context)
    }
}
