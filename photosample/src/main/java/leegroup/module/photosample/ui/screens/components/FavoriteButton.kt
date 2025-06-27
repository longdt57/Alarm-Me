package leegroup.module.photosample.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import leegroup.module.photosample.R

@Composable
internal fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit = {}
) {
    IconButton(
        onClick = { onFavoriteClick() },
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
            contentDescription = if (isFavorite) stringResource(R.string.remove_from_favorites) else
                stringResource(R.string.add_to_favorites),
            tint = if (isFavorite) leegroup.module.designsystem.theme.YellowSoft300 else Color.Gray,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteButtonPreview() {
    FavoriteButton(isFavorite = true)
}

@Preview(showBackground = true)
@Composable
private fun NotFavoriteButtonPreview() {
    FavoriteButton(isFavorite = false)
}
