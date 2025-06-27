package leegroup.module.sample.gituser.domain.params

internal data class GetGitUserListParam(
    val since: Long,
    val perPage: Int,
)
