package leegroup.module.photosample.data.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import leegroup.module.photosample.domain.models.PhotoModelD

@Serializable
@Entity
internal data class PhotoEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "album_id")
    val albumId: Int,

    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "url")
    val url: String,
)

internal fun PhotoEntity.mapToDomain() = PhotoModelD(
    id = id,
    albumId = albumId,
    thumbnailUrl = thumbnailUrl,
    title = title,
    url = url,
)

internal fun List<PhotoEntity>.mapToDomain() = map { it.mapToDomain() }

internal fun PhotoModelD.mapToEntity() = PhotoEntity(
    id = id,
    albumId = albumId,
    thumbnailUrl = thumbnailUrl,
    title = title,
    url = url,
)

internal fun List<PhotoModelD>.mapToEntity() = map { it.mapToEntity() }

