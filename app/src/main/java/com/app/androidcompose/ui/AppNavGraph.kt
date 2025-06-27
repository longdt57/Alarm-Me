package com.app.androidcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.androidcompose.ui.screens.main.MainScreen
import leegroup.module.alarm.ui.alarmNavGraph
import leegroup.module.designsystem.support.extensions.appNavigate
import leegroup.module.designsystem.support.extensions.launchSingleTopNavOptions
import leegroup.module.photosample.ui.screens.main.photoNavGraph
import leegroup.module.sample.gituser.ui.screens.gitUserNavGraph

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        route = AppDestination.RootNavGraph.route,
        startDestination = AppDestination.MainScreen.destination,
        modifier = modifier
    ) {
        composable(AppDestination.MainScreen.route) {
            MainScreen(
                navigator = { destination ->
                    navController.appNavigate(
                        destination,
                        navController.launchSingleTopNavOptions
                    )
                }
            )
        }
        photoNavGraph(navController = navController)
        gitUserNavGraph(navController = navController)
        alarmNavGraph(navController = navController)
    }
}
