package leegroup.module.photosample.domain.usecases.photolist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import leegroup.module.photosample.domain.models.PhotoModelD
import leegroup.module.photosample.domain.params.GetPhotoListParam
import leegroup.module.photosample.domain.repositories.PhotoListRepository
import javax.inject.Inject

internal class GetPhotoListUseCase @Inject constructor(
    private val repository: PhotoListRepository
) {

    operator fun invoke(param: GetPhotoListParam): Flow<List<PhotoModelD>> {
        return flow {
            val localData = getLocal(param)

            if (localData.isNotEmpty()) {
                emit(localData)
            } else {
                val photos = getRemote(param)
                emit(photos)
                saveToLocalIfNeeded(param, photos)
            }
        }
    }

    private suspend fun saveToLocalIfNeeded(
        param: GetPhotoListParam,
        remoteData: List<PhotoModelD>
    ) {
        // Only save to Local if it's normal queries
        if (param.isQueryEmpty() && param.ids.isNullOrEmpty()) {
            repository.saveToLocal(remoteData)
        }
    }

    private suspend fun getLocal(param: GetPhotoListParam): List<PhotoModelD> {
        // Local doesn't support titleLike
        return when {
            param.isQueryEmpty() -> repository.getLocal(param)
            else -> emptyList()
        }
    }

    private suspend fun getRemote(param: GetPhotoListParam): List<PhotoModelD> {
        return repository.getRemote(param)
    }
}