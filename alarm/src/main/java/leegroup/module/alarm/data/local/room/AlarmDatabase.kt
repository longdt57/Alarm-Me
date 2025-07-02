package leegroup.module.alarm.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.alarm.data.models.AlarmModel

@Database(entities = [AlarmModel::class, AlarmAudioModel::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseConverters::class)
internal abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun alarmAudioDao(): AlarmAudioDao
}