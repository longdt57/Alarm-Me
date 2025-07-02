package leegroup.module.alarm.data.repositories

import kotlinx.coroutines.flow.Flow
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.data.models.SampleModel

internal interface AlarmRepository {

    fun observeAlarms(): Flow<List<AlarmModel>>
    suspend fun getEnabledAlarms(): List<AlarmModel>
    suspend fun createAlarm(alarmModel: AlarmModel): AlarmModel
    suspend fun updateAlarm(newAlarmModel: AlarmModel): AlarmModel
    suspend fun enableAlarm(id: Int, enable: Boolean)
    suspend fun deleteAlarm(id: Int)
    suspend fun getAlarmById(id: Int): AlarmModel?
    suspend fun fetchAlarmAudio(): List<AlarmAudioModel>
    fun observeAlarmAudio(): Flow<List<AlarmAudioModel>>
}