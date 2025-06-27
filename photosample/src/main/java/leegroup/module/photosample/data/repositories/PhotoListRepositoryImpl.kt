package leegroup.module.photosample.data.repositories

import leegroup.module.photosample.data.local.room.PhotoDao
import leegroup.module.photosample.data.local.room.entities.mapToDomain
import leegroup.module.photosample.data.local.room.entities.mapToEntity
import leegroup.module.photosample.data.models.mapToDomain
import leegroup.module.photosample.data.remote.services.PhotoApiService
import leegroup.module.photosample.domain.models.PhotoModelD
import leegroup.module.photosample.domain.params.GetPhotoListParam
import leegroup.module.photosample.domain.repositories.PhotoListRepository
import javax.inject.Inject

internal class PhotoListRepositoryImpl @Inject constructor(
    private val appService: PhotoApiService,
    private val photoDao: PhotoDao,
) : PhotoListRepository {

    override suspend fun getRemote(param: GetPhotoListParam): List<PhotoModelD> {
        val items = appService.getPhotos(
            ids = param.ids,
            titleLike = param.titleLike,
            since = param.since,
            limit = param.limit
        )
        return items.mapToDomain()
    }

    override suspend fun getLocal(param: GetPhotoListParam): List<PhotoModelD> {
        val localData = if (param.ids.isNullOrEmpty())
            photoDao.getPhotos(
                since = param.since,
                limit = param.limit
            )
        else
            photoDao.getPhotosByIds(
                since = param.since,
                limit = param.limit,
                ids = param.ids
            )
        return localData.mapToDomain()
    }

    override suspend fun saveToLocal(items: List<PhotoModelD>) {
        photoDao.upsert(items.mapToEntity())
    }
}