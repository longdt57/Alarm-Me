package leegroup.module.alarm.ui.models

import androidx.compose.runtime.Immutable
import leegroup.module.alarm.data.models.AlarmModel

@Immutable
internal data class AlarmUiState(
    val alarms: List<AlarmModel> = emptyList()
)
