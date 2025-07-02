package leegroup.module.alarm.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.alarm.data.repositories.AlarmRepository
import javax.inject.Inject

internal class ObserveAlarmAudioUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {

    fun invoke(): Flow<List<AlarmAudioModel>> = alarmRepository.observeAlarmAudio()
}