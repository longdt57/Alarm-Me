package leegroup.module.core.util

import androidx.lifecycle.SavedStateHandle

interface SavedStateProvider<T> {

    fun toRoute(savedStateHandle: SavedStateHandle): T
}