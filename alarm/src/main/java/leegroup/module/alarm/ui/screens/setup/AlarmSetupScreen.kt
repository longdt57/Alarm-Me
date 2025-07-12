package leegroup.module.alarm.ui.screens.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import leegroup.module.alarm.R
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.alarm.ui.models.AlarmSetupUiState
import leegroup.module.alarm.ui.screens.components.CheckNotificationPermissionAndSettings
import leegroup.module.alarm.ui.screens.components.TimePickerState
import leegroup.module.alarm.ui.screens.components.TimePickerWheel
import leegroup.module.alarm.ui.screens.components.getAlarmRepeatDisplayText
import leegroup.module.alarm.ui.screens.setup.components.AlarmSetupAudioBottomSheet
import leegroup.module.alarm.ui.screens.setup.components.AlarmSetupItemLabel
import leegroup.module.alarm.ui.screens.setup.components.AlarmSetupItemView
import leegroup.module.alarm.ui.screens.setup.components.AlarmSetupRepeatBottomSheet
import leegroup.module.alarm.ui.screens.setup.components.AlarmSetupSnoozeView
import leegroup.module.core.extensions.compose.collectAsEffect
import leegroup.module.designsystem.components.BaseScreen
import leegroup.module.designsystem.components.CenterTopAppBar
import leegroup.module.designsystem.theme.ComposeTheme
import leegroup.module.designsystem.ui.models.BaseDestination

@Composable
fun AlarmSetupScreen(
    modifier: Modifier = Modifier,
    navigator: (destination: Any) -> Unit,
) {
    val viewModel = hiltViewModel<AlarmSetupViewModel>()
    viewModel.navigator.collectAsEffect { destination -> navigator(destination) }

    var bottomSheet: AlarmSetupBottomSheet by remember { mutableStateOf(AlarmSetupBottomSheet.None) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var checkNotificationPermissionAndSave by remember { mutableStateOf(false) }

    when (bottomSheet) {
        AlarmSetupBottomSheet.None -> {}
        AlarmSetupBottomSheet.Repeat -> {
            AlarmSetupRepeatBottomSheet(
                selectedItems = uiState.repeatDays,
                onItemClick = {
                    viewModel.onRepeatItemClick(it)
                },
                onDismissed = {
                    bottomSheet = AlarmSetupBottomSheet.None
                }
            )
        }

        AlarmSetupBottomSheet.CustomAudio -> {
            AlarmSetupAudioBottomSheet(
                options = uiState.audioList,
                selectedItem = uiState.audio,
                onItemClick = {
                    viewModel.onAudioSelected(it)
                },
                onDismissed = {
                    bottomSheet = AlarmSetupBottomSheet.None
                }
            )
        }
    }
    if (checkNotificationPermissionAndSave) {
        CheckNotificationPermissionAndSettings {
            checkNotificationPermissionAndSave = false
            if (it) viewModel.save()
        }
    }

    BaseScreen(viewModel) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            CenterTopAppBar(
                title = stringResource(R.string.setup_alarm),
                onBack = {
                    navigator(BaseDestination.Up())
                })
            AlarmSetupScreenContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                uiState = uiState,
                onRepeatClick = {
                    bottomSheet = AlarmSetupBottomSheet.Repeat
                },
                onSnoozeChange = {
                    viewModel.onSnoozeChange()
                },
                onLabelChange = {
                    viewModel.onTextChange(it)
                },
                onTimeChange = { value ->
                    viewModel.onTimeChange(value)
                },
                onAudioClick = {
                    bottomSheet = AlarmSetupBottomSheet.CustomAudio
                },
                onDeleteClick = {
                    viewModel.deleteAlarm()
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = {
                    checkNotificationPermissionAndSave = true
                }) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun AlarmSetupScreenContent(
    modifier: Modifier = Modifier,
    uiState: AlarmSetupUiState,
    onSnoozeChange: (Boolean) -> Unit = {},
    onRepeatClick: () -> Unit = {},
    onAudioClick: () -> Unit = {},
    onLabelChange: (String) -> Unit = {},
    onTimeChange: (TimePickerState) -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val itemHeight = 40.dp
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimePickerWheel(
            modifier = Modifier.width(220.dp),
            initialTimePickerState = uiState.initialTimePickerState,
            onTimeSelected = onTimeChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        AlarmSetupItemRepeat(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight),
            selectedDays = uiState.repeatDays,
            onItemClick = onRepeatClick
        )
        AlarmSetupItemLabel(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight),
            value = uiState.label,
            onTextChange = { onLabelChange(it) }
        )
        AlarmSetupItemAudio(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight),
            audio = uiState.audio,
            onItemClick = onAudioClick,
        )
        AlarmSetupSnoozeView(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight),
            enabled = uiState.snoozeEnabled,
            onCheckedChange = onSnoozeChange
        )
        if (uiState.isNewAlarm.not()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .padding(top = 4.dp)
                    .clickable {
                        onDeleteClick()
                    },
                text = stringResource(R.string.delete),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFC2B0F),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun AlarmSetupItemRepeat(
    modifier: Modifier = Modifier,
    selectedDays: List<Int>,
    onItemClick: () -> Unit
) {
    val text = getAlarmRepeatDisplayText(selectedDays, stringResource(R.string.never))
    AlarmSetupItemView(
        modifier = modifier,
        title = stringResource(R.string.repeat),
        value = text,
        onItemClick = onItemClick
    )
}

@Composable
private fun AlarmSetupItemAudio(
    modifier: Modifier = Modifier,
    audio: AlarmAudioModel?,
    onItemClick: () -> Unit
) {
    AlarmSetupItemView(
        modifier = modifier,
        title = stringResource(R.string.sound),
        value = audio?.title.orEmpty(),
        onItemClick = onItemClick
    )
}

private sealed interface AlarmSetupBottomSheet {
    data object None : AlarmSetupBottomSheet
    data object Repeat : AlarmSetupBottomSheet
    data object CustomAudio : AlarmSetupBottomSheet
}

@PreviewLightDark
@Composable
private fun AlarmSetupScreenPreview() {
    ComposeTheme {
        AlarmSetupScreenContent(
            uiState = AlarmSetupUiState(),
        )
    }
}
