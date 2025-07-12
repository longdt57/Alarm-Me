package leegroup.module.alarm.ui.screens.setup.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import leegroup.module.alarm.R

@Composable
fun AlarmSetupSnoozeView(
    modifier: Modifier = Modifier, enabled: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.snooze),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )

        Switch(
            checked = enabled,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}