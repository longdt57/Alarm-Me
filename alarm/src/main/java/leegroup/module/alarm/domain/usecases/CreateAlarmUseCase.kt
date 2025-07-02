package leegroup.module.alarm.domain.usecases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.data.repositories.AlarmRepository
import leegroup.module.alarm.support.extensions.setupAlarmClock
import javax.inject.Inject

internal class CreateAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    @ApplicationContext private val context: Context,
) {

    operator fun invoke(alarmModel: AlarmModel) = flow {
        val result = alarmRepository.createAlarm(alarmModel)
        context.setupAlarmClock(result)
        emit(result)
    }
}
