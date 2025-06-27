package leegroup.module.alarm.ui

import leegroup.module.designsystem.ui.models.BaseDestination

sealed class AlarmDestination {
    object SampleScreen : BaseDestination("sampleScreen")
}
