package leegroup.module.photosample.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import leegroup.module.core.extensions.orFalse
import javax.inject.Inject
import javax.inject.Singleton

private const val APP_DATASTORE = "photo-datastore"

@Singleton
internal class PhotoDataStore @Inject constructor(
    context: Context,
) : leegroup.module.data.datastore.BaseDataStore(context, APP_DATASTORE) {

    fun getFavoriteList(): Flow<Set<String>> {
        return getValue(PHOTO_FAVOURITE_LIST).map { it.orEmpty() }
    }

    suspend fun setFavoriteList(value: Set<String>) {
        setValue(PHOTO_FAVOURITE_LIST, value)
    }

    fun getFavoriteFilter(): Flow<Boolean> {
        return getValue(PHOTO_LIST_FAVORITE_FILTER).map { it.orFalse }
    }

    suspend fun setFavoriteFilter(value: Boolean) {
        setValue(PHOTO_LIST_FAVORITE_FILTER, value)
    }

    companion object {
        private val PHOTO_LIST_FAVORITE_FILTER = booleanPreferencesKey("PHOTO_LIST_FAVORITE_FILTER")
        private val PHOTO_FAVOURITE_LIST = stringSetPreferencesKey("PHOTO_FAVOURITE_LIST")
    }
}