package leegroup.module.photosample.ui.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import leegroup.module.photosample.domain.models.PhotoModelD

@Immutable
@Serializable
internal data class PhotoUiModel(
    val id: Int,
    val albumId: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String,
    val isFavorite: Boolean,
) {
    fun switchFavorite() = copy(isFavorite = isFavorite.not())
}

internal fun PhotoModelD.mapToUiModel() = PhotoUiModel(
    id = id,
    albumId = albumId,
    thumbnailUrl = thumbnailUrl,
    title = title,
    url = url,
    isFavorite = false,
)

internal fun List<PhotoModelD>.mapToUiModels() = map { it.mapToUiModel() }

internal fun List<PhotoUiModel>.updateFavorites(favoriteList: Set<Int>): List<PhotoUiModel> {
    return map {
        it.copy(isFavorite = favoriteList.contains(it.id))
    }
}