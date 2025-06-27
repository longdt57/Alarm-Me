package leegroup.module.sample.gituser.data.remote.services

import leegroup.module.sample.gituser.data.models.GitUser
import leegroup.module.sample.gituser.data.models.GitUserDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitUserApiService {

    @GET("users")
    suspend fun getGitUser(
        @Query("since") since: Long,
        @Query("per_page") perPage: Int,
    ): List<GitUser>

    @GET("users/{login}")
    suspend fun getGitUserDetail(
        @Path("login") login: String,
    ): GitUserDetail
}
