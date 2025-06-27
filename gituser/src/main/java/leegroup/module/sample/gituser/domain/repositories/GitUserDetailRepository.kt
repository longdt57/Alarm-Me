package leegroup.module.sample.gituser.domain.repositories

import leegroup.module.sample.gituser.domain.models.GitUserDetailModel

internal interface GitUserDetailRepository {
    suspend fun getRemote(login: String): GitUserDetailModel
    suspend fun getLocal(login: String): GitUserDetailModel?
}