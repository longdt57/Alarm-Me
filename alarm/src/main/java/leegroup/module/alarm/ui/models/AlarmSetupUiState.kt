package leegroup.module.alarm.ui.models

import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.alarm.support.utils.DateTimeUtil
import leegroup.module.alarm.ui.screens.components.TimePickerState

data class AlarmSetupUiState(
    val audio: AlarmAudioModel? = null,
    val id: Int = 0,
    val isEnabled: Boolean = true,
    val label: String = "",
    val repeatDays: List<Int> = emptyList(),
    val snoozeEnabled: Boolean = true,
    val timeOfDay: String = DateTimeUtil.getCurrentAlarmTime(),
    val initialTimePickerState: TimePickerState = TimePickerState.parseTimeToPickerState(timeOfDay),
    val audioList: List<AlarmAudioModel> = emptyList()
) {
    val isNewAlarm = id <= 0
}