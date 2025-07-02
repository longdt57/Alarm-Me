package leegroup.module.alarm.ui.screens.quiz

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.core.extensions.getParcelable
import leegroup.module.core.extensions.putParcelable
import leegroup.module.designsystem.theme.ComposeTheme

@AndroidEntryPoint
class AlarmQuizActivity : ComponentActivity() {

    private val viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOverLockscreen()
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = QuizScreenDestination::class.java.name) {
                    quizScreen(
                        navController = navController,
                    )
                }
            }
        }
    }

    private fun showOverLockscreen() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val alarm = intent.getParcelable<AlarmModel>() ?: return
        viewModel.updateAlarm(alarm)
    }

    companion object {

        fun getAlarmQuizIntent(
            context: Context,
            alarm: AlarmModel,
            shouldOpenAppWhenFinish: Boolean = false
        ): Intent {
            val intent = Intent(context, AlarmQuizActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(KEY_SHOULD_OPEN_APP_WHEN_FINISH, shouldOpenAppWhenFinish)
            }
            intent.putParcelable(alarm)
            return intent
        }
    }
}
