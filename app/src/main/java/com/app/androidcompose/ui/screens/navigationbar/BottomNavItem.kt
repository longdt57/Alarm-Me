package com.app.androidcompose.ui.screens.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.app.androidcompose.R
import com.app.androidcompose.ui.AppDestination
import leegroup.module.photosample.ui.screens.main.PhotoDestination

enum class BottomNavItem(val route: String, val titleRes: Int, val icon: ImageVector) {

    Main(AppDestination.MainScreen.route, R.string.main, Icons.Default.Home),
    Photo(
        PhotoDestination.PhotoList.route,
        leegroup.module.photosample.R.string.photo_list_screen_title,
        Icons.Default.Face
    ),
}