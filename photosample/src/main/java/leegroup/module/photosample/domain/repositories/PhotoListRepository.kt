package leegroup.module.photosample.domain.repositories

import leegroup.module.photosample.domain.models.PhotoModelD
import leegroup.module.photosample.domain.params.GetPhotoListParam

internal interface PhotoListRepository {
    suspend fun getRemote(param: GetPhotoListParam): List<PhotoModelD>
    suspend fun getLocal(param: GetPhotoListParam): List<PhotoModelD>
    suspend fun saveToLocal(items: List<PhotoModelD>)
}