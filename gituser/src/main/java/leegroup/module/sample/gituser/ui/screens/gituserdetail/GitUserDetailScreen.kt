package leegroup.module.sample.gituser.ui.screens.gituserdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import leegroup.module.core.extensions.compose.collectAsEffect
import leegroup.module.designsystem.components.BaseScreen
import leegroup.module.designsystem.ui.models.BaseDestination
import leegroup.module.sample.gituser.ui.models.GitUserDetailUiModel
import leegroup.module.sample.gituser.ui.screens.gituserdetail.components.GitUserDetailAppBar
import leegroup.module.sample.gituser.ui.screens.gituserdetail.components.GitUserDetailBlog
import leegroup.module.sample.gituser.ui.screens.gituserdetail.components.GitUserDetailCard
import leegroup.module.sample.gituser.ui.screens.gituserdetail.components.GitUserDetailFollows

@Composable
internal fun GitUserDetailScreen(
    navigator: (destination: Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: GitUserDetailViewModel = hiltViewModel()
    viewModel.navigator.collectAsEffect { destination -> navigator(destination) }
    val uiModel by viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(viewModel) {
        GitUserDetailScreenContent(
            modifier = modifier,
            uiModel = uiModel,
            onBack = { navigator(BaseDestination.Up()) }
        )
    }
}

@Composable
private fun GitUserDetailScreenContent(
    modifier: Modifier = Modifier,
    uiModel: GitUserDetailUiModel,
    onBack: () -> Unit,
) {
    Column(modifier) {
        GitUserDetailAppBar(onBack = onBack)
        GitUserDetailCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp),
            name = uiModel.name,
            avatarUrl = uiModel.avatarUrl,
            location = uiModel.location
        )
        GitUserDetailFollows(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(horizontal = 80.dp),
            followers = uiModel.followers,
            following = uiModel.following
        )
        GitUserDetailBlog(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp),
            blog = uiModel.blog
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        GitUserDetailScreenContent(
            uiModel = GitUserDetailUiModel(
                name = "Logan Do",
                avatarUrl = "https://avatars.githubusercontent.com/u/8809113?v=4",
                blog = "https://github.com/longdt57",
                location = "Hanoi",
                followers = "100+",
                following = "50+",
            ),
            onBack = {}
        )
    }
}
