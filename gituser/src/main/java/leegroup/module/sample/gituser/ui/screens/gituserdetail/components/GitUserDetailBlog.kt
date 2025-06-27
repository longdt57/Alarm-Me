package leegroup.module.sample.gituser.ui.screens.gituserdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import leegroup.module.sample.gituser.R
import leegroup.module.sample.gituser.support.extensions.formatAndOpenUrl

@Composable
internal fun GitUserDetailBlog(modifier: Modifier = Modifier, blog: String) {
    if (blog.isBlank()) return
    Column(modifier) {
        Text(
            text = stringResource(id = R.string.blog),
            style = MaterialTheme.typography.titleLarge,
        )
        val context = LocalContext.current
        Text(
            modifier = Modifier
                .clickable {
                    context.formatAndOpenUrl(blog)
                }
                .padding(vertical = 8.dp),
            text = blog,
            style = MaterialTheme.typography.bodyLarge,
            color = leegroup.module.designsystem.theme.GreySoft200,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun BlogPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        GitUserDetailBlog(blog = "https://www.google.com")
    }
}