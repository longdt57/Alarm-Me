package leegroup.module.alarm.domain.usecases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import leegroup.module.alarm.data.repositories.AlarmRepository
import leegroup.module.alarm.data.repositories.impl.AlarmRepositoryImpl
import leegroup.module.alarm.support.extensions.setupAlarmClock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AlarmCleanupUseCase @Inject constructor(
    private val alarmRepository: AlarmRepositoryImpl,
    @ApplicationContext private val context: Context,
) {

    suspend fun invoke(alarmId: Int) {
        val alarm = alarmRepository.getAlarmById(alarmId) ?: return

        if (alarm.isRecurring() && alarm.isEnabled) {
            context.setupAlarmClock(alarm)
        } else if (alarm.isEnabled) {
            alarmRepository.enableAlarm(alarmId, enable = false)
        }
    }
}