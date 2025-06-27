package leegroup.module.photosample.domain.usecases.photofavorite

import kotlinx.coroutines.flow.firstOrNull
import leegroup.module.photosample.domain.params.SaveFavoriteParam
import leegroup.module.photosample.domain.repositories.PhotoFavoriteRepository
import javax.inject.Inject

internal class SaveFavoriteUseCase @Inject constructor(
    private val photoFavoriteRepository: PhotoFavoriteRepository
) {
    suspend fun invoke(param: SaveFavoriteParam) {
        val currentFavoriteList = getCurrentFavoriteList()

        when (param.isFavorite) {
            true -> currentFavoriteList.add(param.id)
            false -> currentFavoriteList.remove(param.id)
        }
        photoFavoriteRepository.saveFavoriteList(currentFavoriteList)
    }

    private suspend fun getCurrentFavoriteList() = photoFavoriteRepository
        .getFavoriteList()
        .firstOrNull()
        .orEmpty()
        .toMutableSet()

}