package leegroup.module.alarm.ui.screens.main.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import leegroup.module.alarm.R

@Composable
internal fun AlarmEmptyView(modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = stringResource(R.string.no_alarms))
}