package leegroup.module.alarm.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import leegroup.module.alarm.R
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.ui.AlarmDestination
import leegroup.module.alarm.ui.models.AlarmUiState
import leegroup.module.alarm.ui.screens.components.CheckNotificationPermissionAndSettings
import leegroup.module.alarm.ui.screens.main.components.AlarmEmptyView
import leegroup.module.alarm.ui.screens.main.components.AlarmList
import leegroup.module.core.extensions.compose.collectAsEffect
import leegroup.module.designsystem.components.BaseScreen
import leegroup.module.designsystem.components.CenterTopAppBar
import leegroup.module.designsystem.ui.models.BaseDestination

@Composable
fun AlarmScreen(
    modifier: Modifier = Modifier,
    navigator: (destination: Any) -> Unit,
) {
    val viewModel = hiltViewModel<AlarmViewModel>()
    viewModel.navigator.collectAsEffect { destination -> navigator(destination) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isAnyEnabled = uiState.alarms.any { it.isEnabled }
    if (isAnyEnabled) {
        CheckNotificationPermissionAndSettings {}
    }

    BaseScreen(viewModel) {
        AlarmScreenContent(
            modifier = modifier,
            uiState = uiState,
            onBack = { navigator(BaseDestination.Up()) },
            onAdd = { navigator(AlarmDestination.AlarmSetupDestination()) },
            deleteAlarm = { viewModel.deleteAlarm(it) },
            toggleAlarm = { viewModel.toggleAlarm(it) },
            onClick = { alarm ->
                navigator(AlarmDestination.AlarmSetupDestination(alarmId = alarm.id))
            }
        )
    }
}

@Composable
internal fun AlarmScreenContent(
    modifier: Modifier = Modifier,
    uiState: AlarmUiState,
    onBack: () -> Unit = {},
    onAdd: () -> Unit = {},
    deleteAlarm: (AlarmModel) -> Unit = {},
    toggleAlarm: (AlarmModel) -> Unit = {},
    onClick: (AlarmModel) -> Unit = {},
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CenterTopAppBar(title = stringResource(R.string.alarm_me), actions = {
            IconButton(onClick = onAdd) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        })
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), contentAlignment = Alignment.Center
        ) {
            if (uiState.alarms.isEmpty()) {
                AlarmEmptyView()
            } else {
                AlarmList(
                    modifier = Modifier.fillMaxSize(),
                    alarms = uiState.alarms,
                    onDelete = deleteAlarm,
                    onCheckedChange = toggleAlarm,
                    onClick = onClick
                )
            }
        }
    }
}
