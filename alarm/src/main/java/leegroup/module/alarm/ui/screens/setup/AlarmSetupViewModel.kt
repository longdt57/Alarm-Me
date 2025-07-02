package leegroup.module.alarm.ui.screens.setup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.domain.usecases.CreateAlarmUseCase
import leegroup.module.alarm.domain.usecases.DeleteAlarmUseCase
import leegroup.module.alarm.domain.usecases.FetchAlarmAudioUseCase
import leegroup.module.alarm.domain.usecases.GetAlarmByIdUseCase
import leegroup.module.alarm.domain.usecases.ObserveAlarmAudioUseCase
import leegroup.module.alarm.domain.usecases.UpdateAlarmUseCase
import leegroup.module.alarm.support.utils.DateTimeUtil
import leegroup.module.alarm.ui.AlarmDestination
import leegroup.module.alarm.ui.models.AlarmSetupUiState
import leegroup.module.alarm.ui.screens.components.TimePickerState
import leegroup.module.core.extensions.orFalse
import leegroup.module.core.util.DispatchersProvider
import leegroup.module.designsystem.ui.viewmodel.StateViewModel
import javax.inject.Inject

@HiltViewModel
internal class AlarmSetupViewModel @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    savedStateHandle: SavedStateHandle,
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val createAlarmUseCase: CreateAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val fetchAlarmAudioUseCase: FetchAlarmAudioUseCase,
    private val observeAlarmAudioUseCase: ObserveAlarmAudioUseCase,
) : StateViewModel<AlarmSetupUiState>(AlarmSetupUiState()) {

    init {
        val model = savedStateHandle.toRoute<AlarmDestination.AlarmSetupDestination>()
        if (model.alarmId != null && model.alarmId > 0) { // New alarm setup
            loadAlarmData(model.alarmId)
        }

        fetchAudio()
        observeAudio()
    }

    private fun loadAlarmData(alarmId: Int) {
        getAlarmByIdUseCase.invoke(alarmId)
            .injectLoading()
            .filterNotNull()
            .onEach {
                update { currentState ->
                    currentState.copy(
                        id = it.id,
                        label = it.label.orEmpty(),
                        timeOfDay = it.timeOfDay.orEmpty(),
                        isEnabled = it.isEnabled,
                        snoozeEnabled = it.snoozeEnabled.orFalse,
                        repeatDays = it.repeatDays.orEmpty(),
                        audio = it.audio,
                        initialTimePickerState = TimePickerState.parseTimeToPickerState(it.timeOfDay.orEmpty())
                    )
                }
            }
            .flowOn(dispatchersProvider.io)
            .catchError()
            .launchIn(viewModelScope)
    }

    private fun createAlarm() {
        createAlarmUseCase.invoke(createAlarmRequest())
            .injectLoading()
            .flowOn(dispatchersProvider.io)
            .onEach { navigateUp() }
            .catchError()
            .launchIn(viewModelScope)
    }

    private fun updateAlarm() {
        updateAlarmUseCase.invoke(createAlarmRequest())
            .injectLoading()
            .onEach { navigateUp() }
            .launchIn(viewModelScope)
    }

    private fun createAlarmRequest(): AlarmModel {
        val state = getUiState()
        return AlarmModel(
            id = state.id,
            label = state.label,
            timeOfDay = state.timeOfDay,
            isEnabled = state.isEnabled.orFalse,
            snoozeEnabled = state.snoozeEnabled.orFalse,
            repeatDays = state.repeatDays,
            audio = getUiState().audio,
            createdAt = null,
            updatedAt = null,
            vibrate = true
        )
    }

    fun deleteAlarm() {
        deleteAlarmUseCase.invoke(getUiState().id)
            .injectLoading()
            .flowOn(dispatchersProvider.io)
            .onEach { navigateUp() }
            .catchError()
            .launchIn(viewModelScope)
    }

    fun save() {
        if (isLoading()) return
        update { it.copy(isEnabled = true) }

        if (getUiState().isNewAlarm) {
            createAlarm()
        } else {
            updateAlarm()
        }
    }

    fun onSnoozeChange() {
        update { it.copy(snoozeEnabled = it.snoozeEnabled.not()) }
    }

    fun onTextChange(value: String) {
        update { it.copy(label = value) }
    }

    fun onTimeChange(time: TimePickerState) {
        val value = DateTimeUtil.convertToAlarmSaveTime(time.hour, time.minute, time.period)
        update { it.copy(timeOfDay = value) }
    }

    fun onAudioSelected(audio: AlarmAudioModel) {
        update { it.copy(audio = audio) }
    }

    fun onRepeatItemClick(value: Int) {
        update {
            it.copy(
                repeatDays = if (it.repeatDays.contains(value)) {
                    it.repeatDays - value
                } else {
                    it.repeatDays + value
                }
            )
        }
    }

    private fun fetchAudio() {
        fetchAlarmAudioUseCase.invoke()
            .catchError()
            .launchIn(viewModelScope)
    }

    private fun observeAudio() {
        observeAlarmAudioUseCase.invoke()
            .onEach {
                if (getUiState().audio == null) {
                    update { currentState ->
                        currentState.copy(
                            audio = it.firstOrNull(),
                            audioList = it
                        )
                    }
                } else {
                    update { currentState ->
                        currentState.copy(audioList = it)
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}