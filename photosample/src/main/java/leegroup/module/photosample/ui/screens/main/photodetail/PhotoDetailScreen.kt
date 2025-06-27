package leegroup.module.photosample.ui.screens.main.photodetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import leegroup.module.core.extensions.compose.collectAsEffect
import leegroup.module.designsystem.components.BaseScreen
import leegroup.module.designsystem.ui.models.BaseDestination
import leegroup.module.photosample.ui.models.PhotoDetailUiModel
import leegroup.module.photosample.ui.screens.components.ThumbnailImage
import leegroup.module.photosample.ui.screens.main.photodetail.components.PhotoDetailAppBar

@Composable
internal fun PhotoDetailScreen(
    navigator: (destination: Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: PhotoDetailViewModel = hiltViewModel()
    viewModel.navigator.collectAsEffect { destination -> navigator(destination) }
    val uiModel by viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(viewModel) {
        PhotoDetailContent(
            modifier = modifier,
            uiModel = uiModel,
            onBack = { navigator.invoke(BaseDestination.Up()) },
            onFavoriteClick = { viewModel.switchFavorite() }
        )
    }
}

@Composable
internal fun PhotoDetailContent(
    modifier: Modifier = Modifier,
    uiModel: PhotoDetailUiModel,
    onBack: () -> Unit = {},
    onFavoriteClick: () -> Unit
) {
    Column(modifier = modifier) {
        PhotoDetailAppBar(
            onBack = onBack,
            isFavorite = uiModel.isFavorite,
            onFavoriteClick = onFavoriteClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        ThumbnailImage(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth()
                .aspectRatio(1f),
            url = uiModel.url,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = uiModel.title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoDetailContentPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        PhotoDetailContent(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding(),
            uiModel = PhotoDetailUiModel(
                id = 1,
                title = "Sunset at the Beach",
                url = "https://example.com/sunset.jpg",
                isFavorite = true
            ),
            onFavoriteClick = {}
        )
    }
}
