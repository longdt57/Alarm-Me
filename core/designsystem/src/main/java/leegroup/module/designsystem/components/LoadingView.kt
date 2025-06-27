package leegroup.module.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import leegroup.module.designsystem.ui.models.LoadingState

@Composable
fun LoadingView(loading: LoadingState) {
    when (loading) {
        is LoadingState.Loading -> LoadingProgress(loading)
        else -> {}
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingViewPreview() {
    leegroup.module.designsystem.theme.ComposeTheme {
        LoadingView(LoadingState.Loading())
    }
}