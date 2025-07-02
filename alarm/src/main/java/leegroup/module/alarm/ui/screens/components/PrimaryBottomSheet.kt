package leegroup.module.alarm.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import leegroup.module.designsystem.theme.ComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PrimaryBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    title: String,
    options: List<String>,
    selectedItems: List<String>,
    dismissOnSelected: Boolean = true,
    onItemClick: (String) -> Unit = {},
    onDismissed: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { true },
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    val hideSheetThenInvoke: (call: () -> Unit) -> Unit = { call ->
        scope.launch { sheetState.hide() }
            .invokeOnCompletion {
                call.invoke()
            }
    }

    if (isVisible)
        ModalBottomSheet(
            modifier = modifier.navigationBarsPadding(),
            onDismissRequest = onDismissed,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = { WindowInsets(0) },
        ) {
            PrimaryBottomSheetContent(
                title = title,
                options = options,
                selectedItems = selectedItems
            ) {
                if (dismissOnSelected) {
                    hideSheetThenInvoke { onItemClick(it) }
                } else {
                    onItemClick(it)
                }
            }
        }
}

@Composable
private fun PrimaryBottomSheetContent(
    modifier: Modifier = Modifier,
    title: String,
    options: List<String>,
    selectedItems: List<String>,
    onAlarmTypeSelected: (String) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        options.forEach { option ->
            BottomSheetItemView(
                modifier = Modifier
                    .height(56.dp)
                    .clickable {
                        onAlarmTypeSelected(option)
                    }
                    .padding(horizontal = 20.dp),
                label = option,
                isSelected = selectedItems.contains(option)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun BottomSheetItemView(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f), text = label,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
        )
        if (isSelected)
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Default.Check,
                contentDescription = "",
                tint = Color.Black
            )
    }
}


@PreviewLightDark
@Composable
private fun PrimaryBottomSheetContentPreview() {
    ComposeTheme {
        PrimaryBottomSheetContent(
            title = "DELETE ACCOUNT",
            options = listOf("Option 1", "Option 2", "Option 3"),
            selectedItems = listOf("Option 1"),
        ) {}
    }
}
