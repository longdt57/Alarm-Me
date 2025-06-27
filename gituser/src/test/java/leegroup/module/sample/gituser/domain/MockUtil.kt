package leegroup.module.sample.gituser.domain

import leegroup.module.sample.gituser.domain.models.GitUserDetailModel

internal object MockUtil {
    const val GIT_USER_DETAIL_LOGIN = "longdt57"

    val apiException = RuntimeException()

    val gitUserDetail = GitUserDetailModel(
        id = 8809113,
        login = GIT_USER_DETAIL_LOGIN,
        name = "Logan Do",
        avatarUrl = "https://avatars.githubusercontent.com/u/8809113?v=4",
        blog = "https://github.com/longdt57",
        location = "Hanoi",
        followers = 100_000,
        following = 50_000,
    )
}