package leegroup.module.photosample.ui.screens.main.photolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import leegroup.module.core.extensions.compose.collectAsEffect
import leegroup.module.designsystem.components.BaseScreen
import leegroup.module.designsystem.ui.models.LoadingState
import leegroup.module.photosample.ui.models.PhotoListUiModel
import leegroup.module.photosample.ui.models.PhotoUiModel
import leegroup.module.photosample.ui.screens.components.QueryBox
import leegroup.module.photosample.ui.screens.main.photolist.components.PhotoList
import leegroup.module.photosample.ui.screens.main.photolist.components.PhotoListAppBar
import leegroup.module.photosample.ui.screens.main.photolist.components.PhotoListEmpty

@Composable
internal fun PhotoListScreen(
    navigator: (destination: Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: PhotoListViewModel = hiltViewModel()
    viewModel.navigator.collectAsEffect { destination -> navigator(destination) }

    val uiModel by viewModel.uiState.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    val showRefresh by remember {
        derivedStateOf {
            uiModel.photos.isEmpty() && loading !is LoadingState.Loading
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleActionLoadIfEmpty()
    }

    BaseScreen(viewModel) {
        PhotoListContent(
            modifier = modifier,
            uiModel = uiModel,
            onQueryChange = { query -> viewModel.handleQueryChanged(query) },
            onPhotoClick = { navigator(it) },
            onFavoriteFilterClick = { viewModel.handleFavoriteFilterClick() },
            onFavoriteClick = { viewModel.handleFavoriteClick(it) },
            onLoadMore = { viewModel.loadMore() },
            onRefresh = { viewModel.handleActionLoadIfEmpty() },
            showRefresh = showRefresh
        )
    }
}

@Composable
internal fun PhotoListContent(
    modifier: Modifier = Modifier,
    uiModel: PhotoListUiModel,
    showRefresh: Boolean,
    onQueryChange: (String) -> Unit = {},
    onPhotoClick: (PhotoUiModel) -> Unit = {},
    onFavoriteFilterClick: () -> Unit = {},
    onFavoriteClick: (PhotoUiModel) -> Unit = {},
    onLoadMore: () -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    Column(modifier = modifier) {
        PhotoListAppBar(isFavorite = uiModel.isFavoriteEnabled, onFavoriteClick = {
            onFavoriteFilterClick()
        })
        QueryBox(
            query = uiModel.query,
            onQueryChanged = onQueryChange,
            onClear = { onQueryChange("") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (uiModel.photos.isNotEmpty()) {
            PhotoList(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                photos = uiModel.photos,
                hasMore = uiModel.hasMore,
                onPhotoClick = onPhotoClick,
                onFavoriteClick = onFavoriteClick,
                onLoadMore = onLoadMore,
            )
        } else if (showRefresh) {
            PhotoListEmpty(
                modifier = Modifier.fillMaxSize(),
                onRefresh = onRefresh
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoListContentPreview() {
    val samplePhotos = persistentListOf(
        PhotoUiModel(
            id = 1,
            albumId = 1,
            thumbnailUrl = "https://example.com/thumbnail1.jpg",
            title = "Beautiful Sunset",
            url = "https://example.com/photo1.jpg",
            isFavorite = true
        ),
        PhotoUiModel(
            id = 2,
            albumId = 1,
            thumbnailUrl = "https://example.com/thumbnail2.jpg",
            title = "Mountain View",
            url = "https://example.com/photo2.jpg",
            isFavorite = false
        ),
        PhotoUiModel(
            id = 3,
            albumId = 2,
            thumbnailUrl = "https://example.com/thumbnail3.jpg",
            title = "City Skyline",
            url = "https://example.com/photo3.jpg",
            isFavorite = true
        )
    )
    leegroup.module.designsystem.theme.ComposeTheme {
        PhotoListContent(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding(),
            uiModel = PhotoListUiModel(
                query = "nature",
                photos = samplePhotos,
                hasMore = true
            ),
            showRefresh = false
        )
    }
}