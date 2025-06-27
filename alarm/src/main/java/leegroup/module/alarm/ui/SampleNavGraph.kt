package leegroup.module.alarm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import leegroup.module.alarm.ui.screens.main.AlarmScreen
import leegroup.module.designsystem.support.extensions.appNavigate
import leegroup.module.designsystem.support.extensions.composable

fun NavGraphBuilder.alarmNavGraph(
    navController: NavHostController,
) {
    composable(AlarmDestination.SampleScreen) {
        AlarmScreen(
            navigator = { destination -> navController.appNavigate(destination) },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding(),
        )
    }
}
