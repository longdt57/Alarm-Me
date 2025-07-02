package leegroup.module.alarm.ui.screens.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AppDialog(
    title: String,
    message: String,
    onDismissed: () -> Unit,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    secondaryButtonText: String? = null,
    onSecondaryButtonClick: () -> Unit = onDismissed
) {
    AlertDialog(
        onDismissRequest = onDismissed,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onPrimaryClick) {
                Text(text = primaryButtonText.uppercase())
            }
        },
        dismissButton = {
            secondaryButtonText?.let {
                TextButton(onClick = onSecondaryButtonClick) {
                    Text(text = it.uppercase())
                }
            }
        }
    )
}
