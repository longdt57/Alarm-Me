package leegroup.module.photosample.domain.usecases.photolist

import leegroup.module.photosample.domain.repositories.PhotoListFavoriteFilterRepository
import javax.inject.Inject

internal class GetPhotoListFavoriteFilterUseCase @Inject constructor(
    private val photoListFavoriteFilterRepository: PhotoListFavoriteFilterRepository
) {
    fun invoke() = photoListFavoriteFilterRepository.getFavoriteFilter()
}