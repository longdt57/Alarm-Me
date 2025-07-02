package leegroup.module.alarm.data.local.room

import androidx.room.TypeConverter
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.core.util.JsonUtil

class DatabaseConverters {
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.let { JsonUtil.encodeToString(it) }
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.let {
            JsonUtil.decodeFromString(it)
        }
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { JsonUtil.encodeToString(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let {
            JsonUtil.decodeFromString(it)
        }
    }

    @TypeConverter
    fun fromAudio(value: AlarmAudioModel?): String? {
        return value?.let { JsonUtil.encodeToString(it) }
    }

    @TypeConverter
    fun toAudio(value: String?): AlarmAudioModel? {
        return value?.let {
            JsonUtil.decodeFromString(it)
        }
    }
}