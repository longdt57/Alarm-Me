package leegroup.module.sample.gituser.data.repositories

import leegroup.module.sample.gituser.data.local.room.GitUserDetailDao
import leegroup.module.sample.gituser.data.models.GitUserDetail
import leegroup.module.sample.gituser.data.models.mapToDomain
import leegroup.module.sample.gituser.data.remote.services.GitUserApiService
import leegroup.module.sample.gituser.domain.models.GitUserDetailModel
import leegroup.module.sample.gituser.domain.repositories.GitUserDetailRepository
import javax.inject.Inject

internal class GitUserDetailRepositoryImpl @Inject constructor(
    private val appService: GitUserApiService,
    private val userDao: GitUserDetailDao,
) : GitUserDetailRepository {

    override suspend fun getRemote(login: String): GitUserDetailModel {
        return appService.getGitUserDetail(login).let { userDetail ->
            saveToLocal(userDetail)
            mapToDomain(userDetail)
        }
    }

    override suspend fun getLocal(login: String): GitUserDetailModel? {
        return userDao.getUserDetail(login)?.let { mapToDomain(it) }
    }

    private suspend fun saveToLocal(user: GitUserDetail) {
        userDao.upsert(user)
    }

    private fun mapToDomain(user: GitUserDetail) = user.mapToDomain()
}