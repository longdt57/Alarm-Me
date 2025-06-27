package leegroup.modul.sample.ui

import leegroup.module.designsystem.ui.models.BaseDestination

sealed class SampleDestination {
    object SampleScreen : BaseDestination("sampleScreen")
}
