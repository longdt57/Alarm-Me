package leegroup.module.sample.gituser.ui.screens.gituser.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import leegroup.module.sample.gituser.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GitUserListAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.git_user_list_screen_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        })
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        GitUserListAppBar()
    }
}