package leegroup.module.alarm.data.local.room

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.data.database.dao.BaseDao

@Dao
interface AlarmDao : BaseDao<AlarmModel> {

    @Query("SELECT * FROM AlarmModel ORDER BY timeOfDay ASC")
    fun getAllAsFlow(): Flow<List<AlarmModel>>

    @Query("SELECT * FROM AlarmModel WHERE isEnabled = 1 ORDER BY timeOfDay ASC")
    suspend fun getEnabledAlarms(): List<AlarmModel>

    @Query("SELECT * FROM AlarmModel WHERE id = :id")
    suspend fun getById(id: Int): AlarmModel?

    @Query("UPDATE AlarmModel SET isEnabled = :enable WHERE id = :id")
    suspend fun setEnable(id: Int, enable: Boolean)

    @Query("DELETE FROM AlarmModel WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM AlarmModel")
    suspend fun clearAll()
}
