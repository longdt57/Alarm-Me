package leegroup.module.sample.gituser.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import leegroup.module.designsystem.support.extensions.appNavigate
import leegroup.module.designsystem.support.extensions.composable
import leegroup.module.sample.gituser.ui.screens.gituser.GitUserListScreen
import leegroup.module.sample.gituser.ui.screens.gituserdetail.GitUserDetailScreen

fun NavGraphBuilder.gitUserNavGraph(
    navController: NavHostController,
) {

    navigation(
        route = GitUserDestination.GitUserRoot.route,
        startDestination = GitUserDestination.GitUserList.destination
    ) {
        composable(GitUserDestination.GitUserList) {
            GitUserListScreen(
                navigator = { destination -> navController.appNavigate(destination) },
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
        composable<GitUserDestination.GitUserDetail.GitUserDetailNav> {
            GitUserDetailScreen(
                navigator = { destination ->
                    navController.appNavigate(destination)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .navigationBarsPadding()
                    .statusBarsPadding()
            )
        }
    }
}
