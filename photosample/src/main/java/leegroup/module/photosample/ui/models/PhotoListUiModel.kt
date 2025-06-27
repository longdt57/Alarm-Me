package leegroup.module.photosample.ui.models

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class PhotoListUiModel(
    val query: String = "",
    val isFavoriteEnabled: Boolean = false,
    val photos: ImmutableList<PhotoUiModel> = persistentListOf(),
    val hasMore: Boolean = true,
    val favoriteList: Set<Int> = setOf(),
) {

    fun switchFavoriteFilter() = copy(isFavoriteEnabled = isFavoriteEnabled.not())

    fun resetData() = copy(
        photos = persistentListOf(), hasMore = true
    )

    fun updateQuery(query: String) = copy(query = query)

    fun updateFavorites(favoriteList: Set<Int>): PhotoListUiModel {
        val newPhotos = photos.updateFavorites(favoriteList)
            .filterFavoriteOnly()
            .toImmutableList()
        return copy(
            favoriteList = favoriteList,
            photos = newPhotos
        )
    }

    fun plusPhotos(items: List<PhotoUiModel>): PhotoListUiModel {
        val plusList = items.updateFavorites(favoriteList)
        val newPhotos = photos.plus(plusList)
            .filterFavoriteOnly()
            .toImmutableList()
        return copy(photos = newPhotos)
    }

    private fun List<PhotoUiModel>.filterFavoriteOnly() =
        filter { isFavoriteEnabled.not() || it.isFavorite }
}
