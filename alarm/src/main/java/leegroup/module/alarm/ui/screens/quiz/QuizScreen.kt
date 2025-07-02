package leegroup.module.alarm.ui.screens.quiz

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.Keep
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import kotlinx.serialization.Serializable
import leegroup.module.alarm.R
import leegroup.module.core.extensions.compose.collectAsEffect

@Serializable
@Keep
data object QuizScreenDestination

fun NavGraphBuilder.quizScreen(
    navController: NavHostController,
) {
    composable<QuizScreenDestination> {
        QuizRoute(navController = navController)
    }
}

fun NavController.navigateToQuizScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(QuizScreenDestination) {
        navOptions()
    }
}

@Composable
fun QuizRoute(
    navController: NavHostController,
    viewModel: QuizViewModel = hiltViewModel(LocalActivity.current as ComponentActivity),
) {

    BackHandler(enabled = true) {}
    val activity = LocalActivity.current
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.navigator.collectAsEffect { event ->
        when (event) {
            QuizEvent.Finish -> {
                activity?.finish()
            }
        }
    }

    QuizScreen(
        uiState = uiState, processIntent = viewModel::processIntent
    )
}


@Composable
fun QuizScreen(
    uiState: QuizUiModel, processIntent: (QuizIntent) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {

        AsyncImage(
            model = R.drawable.bg_alarm, contentDescription = "", contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(20.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = uiState.alarmTimeDisplay,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = uiState.alarmLabel,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))


            Box(
                modifier = Modifier
                    .clickable {
                        processIntent.invoke(QuizIntent.OnClickStopButton)
                    }
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    text = stringResource(R.string.stop).uppercase(),
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun QuizScreenPreview() {
    QuizScreen(
        uiState = QuizUiModel(
            alarmTimeDisplay = "08:00 PM",
            alarmLabel = "Wake up",
        ), processIntent = {})
}

