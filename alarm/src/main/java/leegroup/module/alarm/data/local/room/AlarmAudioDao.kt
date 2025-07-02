package leegroup.module.alarm.data.local.room

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.data.database.dao.BaseDao

@Dao
interface AlarmAudioDao : BaseDao<AlarmAudioModel> {

    @Query("SELECT * FROM AlarmAudioModel")
    fun getAllAsFlow(): Flow<List<AlarmAudioModel>>

    @Query("DELETE FROM AlarmAudioModel")
    suspend fun clearAll()
}
