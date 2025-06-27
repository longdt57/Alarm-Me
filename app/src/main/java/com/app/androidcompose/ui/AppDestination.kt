package com.app.androidcompose.ui

import leegroup.module.designsystem.ui.models.BaseDestination

sealed class AppDestination {

    object RootNavGraph : BaseDestination("rootNavGraph")
    object MainScreen : BaseDestination("mainScreen")

}
