package leegroup.module.alarm.domain.usecases

import kotlinx.coroutines.flow.flow
import leegroup.module.alarm.data.repositories.AlarmRepository
import javax.inject.Inject

internal class GetAlarmByIdUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {

    operator fun invoke(id: Int) = flow {
        emit(alarmRepository.getAlarmById(id))
    }
}
