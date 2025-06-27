package leegroup.module.alarm.ui.screens.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import leegroup.module.alarm.data.models.SampleModel
import leegroup.module.alarm.domain.usecases.SampleUseCase
import leegroup.module.alarm.ui.models.AlarmUiState
import leegroup.module.core.util.DispatchersProvider
import leegroup.module.designsystem.ui.viewmodel.StateViewModel
import javax.inject.Inject

@HiltViewModel
internal class AlarmViewModel @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val useCase: SampleUseCase
) : StateViewModel<AlarmUiState>(AlarmUiState()) {

    init {
        loadSample()
    }

    private fun loadSample() {
        useCase.invoke()
            .injectLoading()
            .onEach { sample -> handleSample(sample) }
            .flowOn(dispatchersProvider.io)
            .catch { handleError(it) }
            .launchIn(viewModelScope)
    }

    private fun handleSample(sampleModel: SampleModel) {
        update {
            it.updateSample(sampleModel)
        }
    }
}