package leegroup.module.alarm.data.models


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity
data class AlarmAudioModel(
    @SerialName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @SerialName("duration_s")
    val durationS: Int?,

    @SerialName("file_url")
    val fileUrl: String?,

    @SerialName("title")
    val title: String?
) : Parcelable