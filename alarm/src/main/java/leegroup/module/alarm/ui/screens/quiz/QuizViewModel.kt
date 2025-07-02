package leegroup.module.alarm.ui.screens.quiz

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.support.extensions.stopAlarmService
import leegroup.module.core.extensions.getParcelable
import leegroup.module.core.extensions.orFalse
import leegroup.module.core.util.AppConfigProvider
import leegroup.module.designsystem.ui.viewmodel.StateViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val appConfigProvider: AppConfigProvider
) : StateViewModel<QuizUiModel>(
    QuizUiModel()
) {

    private lateinit var alarm: AlarmModel

    init {
        loadAlarm()
        fetchQuizIfNeed()
    }

    private fun loadAlarm() {
        alarm = savedStateHandle.getParcelable<AlarmModel>() ?: return
        val shouldOpenAppWhenFinish =
            savedStateHandle.get<Boolean>(KEY_SHOULD_OPEN_APP_WHEN_FINISH).orFalse
        update { it.copy(shouldOpenAppWhenFinish = shouldOpenAppWhenFinish) }
        updateAlarm(alarm)
    }

    fun updateAlarm(alarm: AlarmModel) {
        this.alarm = alarm
        val hasQuiz = false
        update {
            it.copy(
                alarmLabel = alarm.label.orEmpty(),
                alarmTimeDisplay = alarm.displayTime
            )
        }

        if (hasQuiz) {
            observeQuiz()
        }
    }

    fun processIntent(intent: QuizIntent) {
        when (intent) {
            is QuizIntent.OnClickAnswerButton -> {
                onClickAnswerButton(intent.selectedAnswer)
            }

            is QuizIntent.OnClickSnoozeButton -> {
                snoozeAlarm()
            }

            is QuizIntent.OnClickStopButton -> {
                stopAlarm()
            }
        }
    }

    private fun onClickAnswerButton(selectedAnswer: String) {
    }

    private fun observeQuiz() {
    }

    private fun fetchQuizIfNeed() {
    }

    private fun snoozeAlarm() {
    }

    private fun stopAlarm() {
        context.stopAlarmService(alarm)
        _navigator.tryEmit(QuizEvent.Finish)
    }
}

sealed interface QuizEvent {
    data object Finish : QuizEvent
}

sealed interface QuizIntent {
    data class OnClickAnswerButton(val selectedAnswer: String) : QuizIntent
    data object OnClickSnoozeButton : QuizIntent
    data object OnClickStopButton : QuizIntent
}
