package leegroup.module.sample.gituser.domain.repositories

import leegroup.module.sample.gituser.domain.models.GitUserModel
import leegroup.module.sample.gituser.domain.params.GetGitUserListParam

internal interface GitUserRepository {
    suspend fun getRemote(param: GetGitUserListParam): List<GitUserModel>
    suspend fun getLocal(param: GetGitUserListParam): List<GitUserModel>
}