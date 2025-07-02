package leegroup.module.alarm.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import leegroup.module.alarm.support.utils.DateTimeUtil

@Serializable
@Parcelize
@Entity
data class AlarmModel(
    @SerialName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @SerialName("audio")
    val audio: AlarmAudioModel? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("is_enabled")
    val isEnabled: Boolean = true,
    @SerialName("label")
    val label: String? = null,
    @SerialName("repeat_days")
    val repeatDays: List<Int>? = null,
    @SerialName("snooze_enabled")
    val snoozeEnabled: Boolean? = null,
    @SerialName("time_of_day")
    val timeOfDay: String? = null, // 22:00:00
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("vibrate")
    val vibrate: Boolean? = null
) : Parcelable {

    val timeInMinutes: Int get() = DateTimeUtil.toTimeInMinutes(timeOfDay.orEmpty()) ?: 0
    val soundUri: String get() = audio?.fileUrl.orEmpty()
    val displayTime: String get() = DateTimeUtil.convertToAlarmDisplayFormat(timeOfDay.orEmpty())

    fun isRecurring() = repeatDays.isNullOrEmpty().not()

    fun isToday() = isRecurring().not() && timeInMinutes > DateTimeUtil.getCurrentDayMinutes()
    fun isTomorrow() = isRecurring().not() && timeInMinutes <= DateTimeUtil.getCurrentDayMinutes()

}
