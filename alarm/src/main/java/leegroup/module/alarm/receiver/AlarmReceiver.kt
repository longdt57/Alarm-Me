package leegroup.module.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.support.extensions.startAlarmService
import leegroup.module.core.extensions.getParcelable

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarm = intent.getParcelable<AlarmModel>() ?: return
        context.startAlarmService(alarm)
    }
}
