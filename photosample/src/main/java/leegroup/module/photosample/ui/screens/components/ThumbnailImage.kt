package leegroup.module.photosample.ui.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import leegroup.module.photosample.R
import leegroup.module.photosample.support.extensions.convertToDummyUrl

@Composable
internal fun ThumbnailImage(modifier: Modifier = Modifier, url: String) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(url.convertToDummyUrl())
            .crossfade(true)
            .build(),
        contentDescription = stringResource(id = R.string.thumbnail_image),
        contentScale = ContentScale.Crop,
    )
}

@Preview(showBackground = true)
@Composable
private fun UserCircleAvatarPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        ThumbnailImage(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            url = "https://via.placeholder.com/600/92c952"
        )
    }
}
