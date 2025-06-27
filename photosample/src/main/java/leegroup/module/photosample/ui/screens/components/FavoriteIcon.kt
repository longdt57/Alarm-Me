package leegroup.module.photosample.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import leegroup.module.photosample.R

@Composable
internal fun FavoriteIcon(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit = {}
) {
    Icon(
        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
        contentDescription = if (isFavorite) stringResource(R.string.remove_from_favorites) else stringResource(
            R.string.add_to_favorites
        ),
        tint = if (isFavorite) leegroup.module.designsystem.theme.YellowSoft300 else Color.Gray,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onFavoriteClick()
            }
    )
}

@Preview(showBackground = true)
@Composable
private fun FavoritePreview() {
    FavoriteIcon(isFavorite = true)
}

@Preview(showBackground = true)
@Composable
private fun NotFavoritePreview() {
    FavoriteIcon(isFavorite = false)
}
