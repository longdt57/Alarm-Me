package leegroup.module.photosample.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import leegroup.module.designsystem.support.extensions.appNavigate
import leegroup.module.designsystem.support.extensions.composable
import leegroup.module.photosample.ui.models.PhotoUiModel
import leegroup.module.photosample.ui.screens.main.photodetail.PhotoDetailScreen
import leegroup.module.photosample.ui.screens.main.photolist.PhotoListScreen

fun NavGraphBuilder.photoNavGraph(
    navController: NavHostController,
) {

    composable(PhotoDestination.PhotoList) {
        PhotoListScreen(
            navigator = { destination ->
                navController.appNavigate(destination)
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
    composable<PhotoUiModel> {
        PhotoDetailScreen(
            navigator = { destination ->
                navController.appNavigate(destination)
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        )
    }
}
