package leegroup.module.photosample.ui.screens.main.photolist.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import leegroup.module.photosample.ui.screens.components.FavoriteButton
import leegroup.module.photosample.ui.screens.components.ThumbnailImage

@Composable
internal fun PhotoListItem(
    modifier: Modifier = Modifier,
    thumbnailUrl: String,
    title: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ThumbnailImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                url = thumbnailUrl,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = Ellipsis
            )
            FavoriteButton(
                modifier = Modifier.size(32.dp),
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
@Suppress("MagicNumber")
private fun Preview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        PhotoListItem(
            thumbnailUrl = "https://via.placeholder.com/150",
            title = LoremIpsum(10).values.joinToString(),
            isFavorite = true,
            onFavoriteClick = {}
        )
    }
}


