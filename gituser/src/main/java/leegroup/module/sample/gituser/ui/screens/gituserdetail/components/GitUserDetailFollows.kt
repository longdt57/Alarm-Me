package leegroup.module.sample.gituser.ui.screens.gituserdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import leegroup.module.sample.gituser.R

@Composable
internal fun GitUserDetailFollows(
    modifier: Modifier = Modifier,
    followers: String,
    following: String
) {
    Row(modifier = modifier) {
        GitUserDetailFollowItem(
            modifier = Modifier.weight(1f),
            value = followers,
            icon = Icons.Filled.AccountCircle,
            text = stringResource(id = R.string.followers)
        )
        Spacer(modifier = Modifier.width(16.dp))
        GitUserDetailFollowItem(
            modifier = Modifier.weight(1f),
            value = following,
            icon = Icons.Filled.Face,
            text = stringResource(id = R.string.following)
        )
    }
}

@Composable
fun GitUserDetailFollowItem(
    modifier: Modifier = Modifier,
    value: String,
    icon: ImageVector = Icons.Filled.AccountCircle,
    text: String
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(48.dp),
            tint = Color.LightGray
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = leegroup.module.designsystem.theme.GreySoft200
        )

    }
}

@Preview(showSystemUi = true)
@Composable
private fun FollowersPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        GitUserDetailFollows(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp),
            followers = "999+",
            following = "99+"
        )
    }
}