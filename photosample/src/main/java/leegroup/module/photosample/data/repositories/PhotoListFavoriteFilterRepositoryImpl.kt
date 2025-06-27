package leegroup.module.photosample.data.repositories

import kotlinx.coroutines.flow.Flow
import leegroup.module.photosample.data.local.datastore.PhotoDataStore
import leegroup.module.photosample.domain.repositories.PhotoListFavoriteFilterRepository
import javax.inject.Inject

internal class PhotoListFavoriteFilterRepositoryImpl @Inject constructor(
    private val dataStore: PhotoDataStore,
) : PhotoListFavoriteFilterRepository {

    override fun getFavoriteFilter(): Flow<Boolean> {
        return dataStore.getFavoriteFilter()
    }

    override suspend fun setFavoriteFilter(value: Boolean) {
        dataStore.setFavoriteFilter(value)
    }

}
