package leegroup.module.alarm.ui

import kotlinx.serialization.Serializable
import leegroup.module.designsystem.ui.models.BaseDestination

sealed class AlarmDestination {
    object AlarmScreen : BaseDestination("alarm_screen")

    data object AlarmRoot : BaseDestination("alarm_root")

    @Serializable
    data class AlarmSetupDestination(
        val alarmId: Int? = null,
    )
}
