package leegroup.module.sample.gituser.ui.screens.gituser

internal sealed interface GitUserListAction {
    data object Refresh : GitUserListAction
    data object LoadIfEmpty : GitUserListAction
    data object LoadMore : GitUserListAction
    data object TrackLaunch : GitUserListAction
    data class TrackOpenUserDetail(val login: String) : GitUserListAction
}