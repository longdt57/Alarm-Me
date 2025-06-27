package leegroup.module.designsystem.ui.viewmodel

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("PropertyName", "MemberVisibilityCanBePrivate")
abstract class SavedStateBaseViewModel<T : Parcelable>(
    private val savedStateHandle: SavedStateHandle,
    initialUiState: T
) : StateViewModel<T>(initialUiState) {

    private val uiStateKey = "${KEY_UI_STATE}_${initialUiState::class.simpleName.orEmpty()}"

    // Retrieve the persisted UI state from SavedStateHandle
    override val _uiState: MutableStateFlow<T> =
        MutableStateFlow(savedStateHandle.get<T>(uiStateKey) ?: initialUiState)

    protected open fun updateAndSave(function: (T) -> T) {
        val currentValue = getUiState()
        val newValue = function(currentValue)

        if (currentValue != newValue) {
            _uiState.value = newValue
            savedStateHandle[uiStateKey] = newValue
        }
    }

    companion object {
        private const val KEY_UI_STATE = "uiState"
    }
}
