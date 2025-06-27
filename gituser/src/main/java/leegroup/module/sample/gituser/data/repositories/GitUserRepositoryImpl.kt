package leegroup.module.sample.gituser.data.repositories

import leegroup.module.sample.gituser.data.local.room.GitUserDao
import leegroup.module.sample.gituser.data.models.GitUser
import leegroup.module.sample.gituser.data.models.mapToDomain
import leegroup.module.sample.gituser.data.remote.services.GitUserApiService
import leegroup.module.sample.gituser.domain.models.GitUserModel
import leegroup.module.sample.gituser.domain.params.GetGitUserListParam
import leegroup.module.sample.gituser.domain.repositories.GitUserRepository
import javax.inject.Inject

internal class GitUserRepositoryImpl @Inject constructor(
    private val appService: GitUserApiService,
    private val userDao: GitUserDao,
) : GitUserRepository {

    override suspend fun getRemote(param: GetGitUserListParam): List<GitUserModel> {
        val users = appService.getGitUser(since = param.since, perPage = param.perPage)
        saveToLocal(users)
        return mapToDomain(users)
    }

    override suspend fun getLocal(param: GetGitUserListParam): List<GitUserModel> {
        return userDao
            .getUsers(since = param.since, perPage = param.perPage)
            .let { mapToDomain(it) }
    }

    private suspend fun saveToLocal(users: List<GitUser>) {
        userDao.upsert(users)
    }

    private fun mapToDomain(users: List<GitUser>): List<GitUserModel> {
        return users.mapToDomain()
    }
}