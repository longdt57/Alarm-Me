package leegroup.module.designsystem.ui.models

import androidx.annotation.StringRes
import leegroup.module.designsystem.R

sealed interface LoadingState {
    data object None : LoadingState
    data class Loading(@StringRes val messageRes: Int = R.string.loading) : LoadingState
}