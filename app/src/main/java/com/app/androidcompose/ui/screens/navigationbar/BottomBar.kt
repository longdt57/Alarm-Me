package com.app.androidcompose.ui.screens.navigationbar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import leegroup.module.designsystem.support.extensions.launchSingleTopNavOptions

@Composable
fun BottomBar(navController: NavHostController) {
    val items = BottomNavItem.entries
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val title = stringResource(item.titleRes)
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = title) },
                label = { Text(text = title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route, navController.launchSingleTopNavOptions)
                }
            )
        }
    }
}