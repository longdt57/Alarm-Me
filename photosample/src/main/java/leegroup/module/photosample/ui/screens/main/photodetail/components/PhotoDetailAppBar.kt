package leegroup.module.photosample.ui.screens.main.photodetail.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import leegroup.module.photosample.R
import leegroup.module.photosample.ui.screens.components.FavoriteIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PhotoDetailAppBar(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onBack: () -> Unit = {},
    onFavoriteClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.photo_detail_screen_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        },
        actions = {
            FavoriteIcon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp),
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick
            )
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        PhotoDetailAppBar(isFavorite = false, onFavoriteClick = {})
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenFavoritePreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        PhotoDetailAppBar(isFavorite = true, onFavoriteClick = {})
    }
}