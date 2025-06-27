package leegroup.module.photosample.ui.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import leegroup.module.photosample.ui.screens.main.PhotoDetailNav

@Immutable
@Serializable
internal data class PhotoDetailUiModel(
    val id: Int = 0,
    val title: String = "",
    val url: String = "",
    val isFavorite: Boolean = false,
) {

    fun update(photoDetailNav: PhotoDetailNav) = copy(
        id = photoDetailNav.id,
        title = photoDetailNav.title,
        url = photoDetailNav.url,
        isFavorite = photoDetailNav.isFavorite
    )

    fun reverseFavorite() = copy(isFavorite = isFavorite.not())
}
