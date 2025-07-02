package leegroup.module.alarm.domain.usecases

import leegroup.module.alarm.data.repositories.AlarmRepository
import javax.inject.Inject

internal class ObserveAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {

    operator fun invoke() = alarmRepository.observeAlarms()
}
