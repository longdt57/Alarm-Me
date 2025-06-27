package leegroup.module.sample.gituser.data.remote.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import leegroup.module.sample.gituser.data.models.GitUser
import leegroup.module.sample.gituser.data.models.GitUserDetail
import leegroup.module.sample.gituser.di.data.GitUserKtorHttpClientModule.Companion.GIT_USER_KTOR_HTTP_CLIENT
import javax.inject.Inject
import javax.inject.Named

internal class GitUserApiServiceImpl @Inject constructor(
    @Named(GIT_USER_KTOR_HTTP_CLIENT) private val httpClient: HttpClient
) : GitUserApiService {
    override suspend fun getGitUser(
        since: Long,
        perPage: Int
    ): List<GitUser> {
        return httpClient.get(GET_USER) {
            url {
                parameters.append("since", since.toString())
                parameters.append("per_page", perPage.toString())
            }
        }.body()
    }

    override suspend fun getGitUserDetail(login: String): GitUserDetail {
        return httpClient.get(GET_USER_DETAIL + login).body()
    }

    companion object {
        private const val GET_USER = "users"
        private const val GET_USER_DETAIL = "users/"
    }
}