package leegroup.module.photosample.domain.usecases.photofavorite

import leegroup.module.photosample.domain.repositories.PhotoFavoriteRepository
import javax.inject.Inject

internal class ObserveFavoriteListUseCase @Inject constructor(
    private val photoFavoriteRepository: PhotoFavoriteRepository
) {
    fun invoke() = photoFavoriteRepository.getFavoriteList()
}