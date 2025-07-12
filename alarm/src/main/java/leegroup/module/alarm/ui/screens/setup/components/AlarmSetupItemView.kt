package leegroup.module.alarm.ui.screens.setup.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun AlarmSetupItemView(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onItemClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.clickable {
            onItemClick()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
        )
        Text(
            modifier = Modifier
                .padding(end = 8.dp, start = 16.dp)
                .weight(1f),
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End
        )
    }
}