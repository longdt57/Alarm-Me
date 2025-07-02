package leegroup.module.alarm.domain.usecases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import leegroup.module.alarm.data.repositories.AlarmRepository
import leegroup.module.alarm.support.extensions.cancelAlarm
import javax.inject.Inject

internal class DeleteAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    @ApplicationContext private val context: Context
) {

    operator fun invoke(alarmId: Int) = flow {
        val alarm = alarmRepository.getAlarmById(alarmId) ?: return@flow
        context.cancelAlarm(alarm)
        emit(alarmRepository.deleteAlarm(alarm.id))
    }
}
