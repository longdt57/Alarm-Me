package leegroup.module.sample.gituser.domain.repositories

import kotlinx.coroutines.flow.Flow

internal interface GitUserPreferencesRepository {

    fun getAppPreference(): Flow<Boolean>

    suspend fun updateAppPreference(appPreferencesValue: Boolean)
}
