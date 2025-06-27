package leegroup.modul.sample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import leegroup.modul.sample.ui.screens.main.SampleScreen
import leegroup.module.designsystem.support.extensions.appNavigate
import leegroup.module.designsystem.support.extensions.composable

fun NavGraphBuilder.sampleNavGraph(
    navController: NavHostController,
) {
    composable(SampleDestination.SampleScreen) {
        SampleScreen(
            navigator = { destination -> navController.appNavigate(destination) },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding(),
        )
    }
}
