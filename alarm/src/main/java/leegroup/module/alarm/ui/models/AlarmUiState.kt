package leegroup.module.alarm.ui.models

import androidx.compose.runtime.Immutable
import leegroup.module.alarm.data.models.SampleModel

@Immutable
internal data class AlarmUiState(
    val id: Int = 0
) {

    fun updateSample(sampleModel: SampleModel): AlarmUiState {
        return copy(id = sampleModel.id)
    }
}
