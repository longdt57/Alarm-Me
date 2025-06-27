package leegroup.module.designsystem.support.extensions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import leegroup.module.designsystem.ui.models.BaseDestination

fun NavGraphBuilder.composable(
    destination: BaseDestination,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = destination.route,
        arguments = destination.arguments,
        deepLinks = destination.deepLinks.map {
            navDeepLink {
                uriPattern = it
            }
        },
        content = content
    )
}

fun NavHostController.appNavigate(destination: Any, navOptions: NavOptions = navOptions {}) {
    when (destination) {
        is BaseDestination -> navigateWithDestination(destination, navOptions)
        else -> navigate(destination, navOptions)
    }
}


fun NavHostController.navigateWithDestination(
    destination: BaseDestination,
    navOptions: NavOptions
) {
    when (destination) {
        is BaseDestination.Up -> {
            destination.results.forEach { (key, value) ->
                previousBackStackEntry?.savedStateHandle?.set(key, value)
            }
            navigateUp()
        }

        else -> navigate(route = destination.destination, navOptions)
    }
}

val NavHostController.launchSingleTopNavOptions
    get() = navOptions {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

fun NavOptionsBuilder.launchSingleTop(navController: NavController) {
    popUpTo(navController.graph.startDestinationId) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}
