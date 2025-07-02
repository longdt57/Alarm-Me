package leegroup.module.alarm.domain.usecases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import leegroup.module.alarm.data.repositories.impl.AlarmRepositoryImpl
import leegroup.module.alarm.support.extensions.setupAlarmClock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RescheduleEnabledAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepositoryImpl,
    @ApplicationContext private val context: Context,
) {

    suspend fun invoke() {
        alarmRepository.getEnabledAlarms()
            .forEach { alarm ->
                context.setupAlarmClock(alarm = alarm)
            }
    }
}