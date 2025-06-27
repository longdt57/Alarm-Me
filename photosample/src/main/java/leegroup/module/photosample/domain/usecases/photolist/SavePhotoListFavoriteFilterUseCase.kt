package leegroup.module.photosample.domain.usecases.photolist

import leegroup.module.photosample.domain.repositories.PhotoListFavoriteFilterRepository
import javax.inject.Inject

internal class SavePhotoListFavoriteFilterUseCase @Inject constructor(
    private val photoListFavoriteFilterRepository: PhotoListFavoriteFilterRepository
) {
    suspend fun invoke(isEnable: Boolean) {
        photoListFavoriteFilterRepository.setFavoriteFilter(isEnable)
    }


}