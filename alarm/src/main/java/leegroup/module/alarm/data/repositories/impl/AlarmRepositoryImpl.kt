package leegroup.module.alarm.data.repositories.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import leegroup.module.alarm.data.local.room.AlarmAudioDao
import leegroup.module.alarm.data.local.room.AlarmDao
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.data.repositories.AlarmRepository
import leegroup.module.core.util.DispatchersProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao,
    private val alarmAudioDao: AlarmAudioDao,
    private val dispatchersProvider: DispatchersProvider
) : AlarmRepository {

    override fun observeAlarms(): Flow<List<AlarmModel>> {
        return alarmDao.getAllAsFlow().flowOn(dispatchersProvider.io)
    }

    override suspend fun getEnabledAlarms(): List<AlarmModel> {
        return alarmDao.getEnabledAlarms()
    }

    override suspend fun createAlarm(alarmModel: AlarmModel): AlarmModel =
        withContext(dispatchersProvider.io) {
            val newId = alarmDao.upsert(alarmModel)
            alarmModel.copy(id = newId.toInt())
        }

    override suspend fun updateAlarm(newAlarmModel: AlarmModel) =
        withContext(dispatchersProvider.io) {
            alarmDao.upsert(newAlarmModel)
            newAlarmModel
        }

    override suspend fun enableAlarm(id: Int, enable: Boolean) =
        withContext(dispatchersProvider.io) {
            alarmDao.setEnable(id, enable)
        }

    override suspend fun deleteAlarm(id: Int) = withContext(dispatchersProvider.io) {
        alarmDao.deleteById(id)
    }

    override suspend fun getAlarmById(id: Int): AlarmModel? = withContext(dispatchersProvider.io) {
        alarmDao.getById(id)
    }

    override suspend fun fetchAlarmAudio(): List<AlarmAudioModel> =
        withContext(dispatchersProvider.io) {
            sampleAudios.apply {
                alarmAudioDao.clearAll()
                alarmAudioDao.upsert(this)
            }
        }

    override fun observeAlarmAudio(): Flow<List<AlarmAudioModel>> {
        return alarmAudioDao.getAllAsFlow().flowOn(dispatchersProvider.io)
    }
}

val sampleAudios = listOf(
    AlarmAudioModel(
        id = 1,
        durationS = 10,
        fileUrl = "https://archive.org/download/alarm-morning-flower/Alarm_Morning_flower.ogg",
        title = "Morning Flower (Samsung)"
    ),
    AlarmAudioModel(
        id = 3,
        durationS = null,
        fileUrl = "https://archive.org/download/morning-alarm-ringtone/Morning%20Alarm%20Ringtone.mp3",
        title = "Morning Alarm Ringtone"
    ),
    AlarmAudioModel(
        id = 5,
        durationS = 14,
        fileUrl = "https://commons.wikimedia.org/wiki/Special:Redirect/file/Alarmclock-mechanical.ogg",
        title = "Mechanical Alarm Clock"
    ),
    AlarmAudioModel(
        id = 6,
        durationS = 8,
        fileUrl = "https://commons.wikimedia.org/wiki/Special:Redirect/file/NFPA_Fire_Alarm.ogg",
        title = "Fire Alarm Tone (NFPA Standard)"
    )
)
