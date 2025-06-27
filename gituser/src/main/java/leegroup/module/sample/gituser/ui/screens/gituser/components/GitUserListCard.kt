package leegroup.module.sample.gituser.ui.screens.gituser.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import leegroup.module.sample.gituser.domain.models.GitUserModel
import leegroup.module.sample.gituser.ui.components.UserCard

@Composable
internal fun GitUserListCard(user: GitUserModel, onClick: (GitUserModel) -> Unit) {
    UserCard(modifier = Modifier.clickable { onClick(user) }) {
        GitUserListItem(
            modifier = Modifier.padding(12.dp),
            title = user.login,
            avatarUrl = user.avatarUrl,
            htmlUrl = user.htmlUrl
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        GitUserListCard(
            user = GitUserModel(
                id = 1,
                login = "longdt57",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                htmlUrl = "https://github.com/longdt57"
            ),
            onClick = {},
        )
    }
}
