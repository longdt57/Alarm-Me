package leegroup.module.alarm.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.alarm.data.repositories.AlarmRepository
import leegroup.module.alarm.filemanager.AlarmAudioDownloadManager
import javax.inject.Inject

internal class FetchAlarmAudioUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmAudioDownloadManager: AlarmAudioDownloadManager
) {

    fun invoke(): Flow<List<AlarmAudioModel>> = flow {
        val audios = alarmRepository.fetchAlarmAudio()
        alarmAudioDownloadManager.preloadAudios(audios.map { it.fileUrl.orEmpty() })
        emit(audios)
    }
}