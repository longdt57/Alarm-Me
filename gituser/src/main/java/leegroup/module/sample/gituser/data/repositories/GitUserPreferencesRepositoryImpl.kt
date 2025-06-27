package leegroup.module.sample.gituser.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import leegroup.module.sample.gituser.data.local.datastore.GitUserDataStore
import leegroup.module.sample.gituser.domain.repositories.GitUserPreferencesRepository
import javax.inject.Inject

internal class GitUserPreferencesRepositoryImpl @Inject internal constructor(
    private val appDataStore: GitUserDataStore
) : GitUserPreferencesRepository {

    override fun getAppPreference(): Flow<Boolean> {
        return appDataStore.getAppPreference()
            .map { it ?: true }
    }

    override suspend fun updateAppPreference(appPreferencesValue: Boolean) {
        appDataStore.setAppPreference(appPreferencesValue)
    }
}
