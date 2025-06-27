package leegroup.modul.sample.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import leegroup.modul.sample.ui.models.SampleUiState
import leegroup.module.core.extensions.compose.collectAsEffect
import leegroup.module.designsystem.components.BaseScreen
import leegroup.module.designsystem.components.CenterTopAppBar
import leegroup.module.designsystem.ui.models.BaseDestination

@Composable
fun SampleScreen(
    modifier: Modifier = Modifier,
    navigator: (destination: Any) -> Unit,
) {
    val viewModel = hiltViewModel<SampleViewModel>()
    viewModel.navigator.collectAsEffect { destination -> navigator(destination) }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(viewModel) {
        SampleContent(
            modifier = modifier,
            uiState = uiState.value,
            onBack = { navigator(BaseDestination.Up()) })
    }
}

@Composable
internal fun SampleContent(
    uiState: SampleUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CenterTopAppBar(title = "Sample", onBack = onBack)
        Text(text = uiState.id.toString(), modifier = Modifier.padding(top = 24.dp))
    }
}