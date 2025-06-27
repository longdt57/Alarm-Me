package leegroup.module.sample.gituser.ui.screens.gituser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import leegroup.module.core.extensions.compose.collectAsEffect
import leegroup.module.designsystem.components.BaseScreen
import leegroup.module.designsystem.ui.models.LoadingState
import leegroup.module.sample.gituser.domain.models.GitUserModel
import leegroup.module.sample.gituser.ui.screens.GitUserDestination
import leegroup.module.sample.gituser.ui.screens.gituser.components.GitUserList
import leegroup.module.sample.gituser.ui.screens.gituser.components.GitUserListAppBar
import leegroup.module.sample.gituser.ui.screens.gituser.components.GitUserListEmpty

@Composable
fun GitUserListScreen(
    modifier: Modifier = Modifier,
    navigator: (destination: Any) -> Unit,
) {
    val viewModel: GitUserListViewModel = hiltViewModel()
    viewModel.navigator.collectAsEffect { destination -> navigator(destination) }
    val uiModel by viewModel.uiState.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val showRefresh by remember {
        derivedStateOf {
            uiModel.users.isEmpty() && loading !is LoadingState.Loading
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleAction(GitUserListAction.LoadIfEmpty)
    }
    LaunchedEffect(Unit) {
        viewModel.handleAction(GitUserListAction.TrackLaunch)
    }

    BaseScreen(viewModel) {
        GitUserListScreenContent(
            modifier = modifier,
            showRefresh = showRefresh,
            users = uiModel.users,
            onLoadMore = {
                viewModel.handleAction(GitUserListAction.LoadMore)
            },
            onClick = { user ->
                viewModel.handleAction(GitUserListAction.TrackOpenUserDetail(user.login))
                navigator(GitUserDestination.GitUserDetail.GitUserDetailNav(user.login))
            },
            onRefresh = {
                viewModel.handleAction(GitUserListAction.Refresh)
            }
        )
    }
}

@Composable
@Suppress("LongParameterList")
private fun GitUserListScreenContent(
    modifier: Modifier = Modifier,
    showRefresh: Boolean,
    users: ImmutableList<GitUserModel>,
    onClick: (GitUserModel) -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
) {
    Column(modifier = modifier) {
        GitUserListAppBar()
        if (users.isNotEmpty()) {
            GitUserList(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                users = users,
                onClick = onClick,
                onLoadMore = onLoadMore,
                onRefresh = onRefresh,
            )
        } else if (showRefresh) {
            GitUserListEmpty(
                modifier = Modifier.fillMaxSize(),
                onRefresh = onRefresh
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenEmptyPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        GitUserListScreenContent(
            showRefresh = true,
            users = persistentListOf(),
            onClick = {},
            onLoadMore = {},
            onRefresh = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        GitUserListScreenContent(
            showRefresh = false,
            users = persistentListOf(
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
            ),
            onClick = {},
            onLoadMore = {},
            onRefresh = {}
        )
    }
}
