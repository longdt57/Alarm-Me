package leegroup.module.sample.gituser.data

import leegroup.module.sample.gituser.data.models.GitUser
import leegroup.module.sample.gituser.data.models.GitUserDetail
import leegroup.module.sample.gituser.domain.models.GitUserDetailModel
import leegroup.module.sample.gituser.domain.models.GitUserModel
import leegroup.module.sample.gituser.domain.params.GetGitUserListParam

internal object MockUtil {
    const val GIT_USER_DETAIL_LOGIN = "longdt57"
    const val GIT_USER_SINCE = 0L
    const val GIT_USER_PER_PAGE = 3

    val param = GetGitUserListParam(since = GIT_USER_SINCE, perPage = GIT_USER_PER_PAGE)

    val gitUserDetail = GitUserDetail(
        id = 8809113,
        login = GIT_USER_DETAIL_LOGIN,
        name = "Logan Do",
        avatarUrl = "https://avatars.githubusercontent.com/u/8809113?v=4",
        blog = "https://github.com/longdt57",
        location = "Hanoi",
        followers = 100_000,
        following = 50_000
    )

    val gitUserDetailModel = GitUserDetailModel(
        id = 8809113,
        login = GIT_USER_DETAIL_LOGIN,
        name = "Logan Do",
        avatarUrl = "https://avatars.githubusercontent.com/u/8809113?v=4",
        blog = "https://github.com/longdt57",
        location = "Hanoi",
        followers = 100_000,
        following = 50_000,
    )

    val sampleGitUsers = listOf(
        GitUser(
            id = 1,
            login = "longdt57",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            htmlUrl = "https://github.com/longdt57"
        ),
        GitUser(
            id = 2,
            login = "defunkt",
            avatarUrl = "https://avatars.githubusercontent.com/u/2?v=4",
            htmlUrl = "https://github.com/defunkt"
        ),
        GitUser(
            id = 3,
            login = "pjhyett",
            avatarUrl = "https://avatars.githubusercontent.com/u/3?v=4",
            htmlUrl = "https://github.com/pjhyett"
        )
    )

    val sampleGitUserModels = listOf(
        GitUserModel(
            id = 1,
            login = "longdt57",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            htmlUrl = "https://github.com/longdt57"
        ),
        GitUserModel(
            id = 2,
            login = "defunkt",
            avatarUrl = "https://avatars.githubusercontent.com/u/2?v=4",
            htmlUrl = "https://github.com/defunkt"
        ),
        GitUserModel(
            id = 3,
            login = "pjhyett",
            avatarUrl = "https://avatars.githubusercontent.com/u/3?v=4",
            htmlUrl = "https://github.com/pjhyett"
        )
    )
}