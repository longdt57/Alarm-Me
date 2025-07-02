package leegroup.module.alarm.ui.screens.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.domain.usecases.DeleteAlarmUseCase
import leegroup.module.alarm.domain.usecases.ObserveAlarmsUseCase
import leegroup.module.alarm.domain.usecases.UpdateAlarmUseCase
import leegroup.module.alarm.ui.models.AlarmUiState
import leegroup.module.core.util.DispatchersProvider
import leegroup.module.designsystem.ui.viewmodel.StateViewModel
import javax.inject.Inject

@HiltViewModel
internal class AlarmViewModel @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val observeAlarmsUseCase: ObserveAlarmsUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val toggleAlarmUseCase: UpdateAlarmUseCase,
) : StateViewModel<AlarmUiState>(AlarmUiState()) {

    init {
        observeAlarms()
    }

    fun deleteAlarm(alarm: AlarmModel) {
        deleteAlarmUseCase.invoke(alarm.id)
            .injectLoading()
            .flowOn(dispatchersProvider.io)
            .catchError()
            .launchIn(viewModelScope)
    }

    private fun observeAlarms() {
        observeAlarmsUseCase.invoke()
            .onEach { alarms -> update { it.copy(alarms = alarms) } }
            .flowOn(dispatchersProvider.io)
            .catchError()
            .launchIn(viewModelScope)
    }

    fun toggleAlarm(alarm: AlarmModel) {
        toggleAlarmUseCase.invoke(alarm.copy(isEnabled = alarm.isEnabled.not()))
            .injectLoading()
            .flowOn(dispatchersProvider.io)
            .catchError()
            .launchIn(viewModelScope)
    }
}