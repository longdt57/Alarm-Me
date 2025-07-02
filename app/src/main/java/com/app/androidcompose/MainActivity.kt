package com.app.androidcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import leegroup.module.alarm.ui.AlarmDestination
import leegroup.module.alarm.ui.alarmNavGraph
import leegroup.module.designsystem.theme.ComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    route = AlarmDestination.AlarmRoot.destination,
                    startDestination = AlarmDestination.AlarmScreen.destination,
                ) {
                    alarmNavGraph(navController = navController)
                }
            }
        }
    }
}
