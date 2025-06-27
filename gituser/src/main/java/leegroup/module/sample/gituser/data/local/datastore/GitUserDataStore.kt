package leegroup.module.sample.gituser.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import leegroup.module.data.datastore.BaseDataStore
import javax.inject.Inject
import javax.inject.Singleton

private const val APP_DATASTORE = "git-user-datastore"

@Singleton
internal class GitUserDataStore @Inject constructor(
    @ApplicationContext context: Context,
) : BaseDataStore(context, APP_DATASTORE) {

    fun getAppPreference(): Flow<Boolean?> {
        return getValue(APP_PREFERENCE)
    }

    suspend fun setAppPreference(value: Boolean) {
        setValue(APP_PREFERENCE, value)
    }

    companion object {
        private val APP_PREFERENCE = booleanPreferencesKey("APP_PREFERENCE")
    }
}