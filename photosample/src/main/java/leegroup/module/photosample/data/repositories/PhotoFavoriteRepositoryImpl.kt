package leegroup.module.photosample.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import leegroup.module.photosample.data.local.datastore.PhotoDataStore
import leegroup.module.photosample.domain.repositories.PhotoFavoriteRepository
import javax.inject.Inject

internal class PhotoFavoriteRepositoryImpl @Inject constructor(
    private val dataStore: PhotoDataStore,
) : PhotoFavoriteRepository {
    override fun getFavoriteList(): Flow<Set<Int>> {
        return dataStore.getFavoriteList().map { items -> items.map { it.toInt() }.toSet() }
    }

    override suspend fun saveFavoriteList(ids: Set<Int>) {
        dataStore.setFavoriteList(ids.map { it.toString() }.toSet())
    }
}
