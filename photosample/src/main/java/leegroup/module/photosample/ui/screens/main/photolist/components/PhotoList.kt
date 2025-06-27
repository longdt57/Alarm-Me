package leegroup.module.photosample.ui.screens.main.photolist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import leegroup.module.designsystem.components.LoadMore
import leegroup.module.photosample.ui.models.PhotoUiModel

@Composable
@Suppress("LongParameterList")
internal fun PhotoList(
    modifier: Modifier = Modifier,
    photos: ImmutableList<PhotoUiModel>,
    hasMore: Boolean,
    onPhotoClick: (PhotoUiModel) -> Unit,
    onFavoriteClick: (PhotoUiModel) -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()
    if (hasMore) {
        LoadMore(listState = listState, onLoadMore = onLoadMore)
    }
    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(photos, key = { it.id }) { photo ->
            PhotoListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onPhotoClick(photo)
                    },
                thumbnailUrl = photo.thumbnailUrl,
                title = photo.title,
                isFavorite = photo.isFavorite,
                onFavoriteClick = {
                    onFavoriteClick(photo)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}