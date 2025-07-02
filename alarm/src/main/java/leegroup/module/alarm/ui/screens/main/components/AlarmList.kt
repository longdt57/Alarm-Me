package leegroup.module.alarm.ui.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import leegroup.module.alarm.R
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.ui.screens.components.SwipeToRevealOption
import leegroup.module.alarm.ui.screens.components.getAlarmRepeatDisplayText

@Composable
internal fun AlarmList(
    modifier: Modifier,
    alarms: List<AlarmModel> = emptyList(),
    onDelete: (AlarmModel) -> Unit = {},
    onCheckedChange: (AlarmModel) -> Unit = {},
    onClick: (AlarmModel) -> Unit = {},
) {

    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        items(alarms, key = { it.id }) { alarm ->
            SwipeToRevealDeleteItem(
                alarm = alarm,
                onItemClick = onClick,
                onCheckedChange = onCheckedChange,
                onDelete = onDelete
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SwipeToRevealDeleteItem(
    alarm: AlarmModel,
    onItemClick: (AlarmModel) -> Unit,
    onCheckedChange: (AlarmModel) -> Unit,
    onDelete: (AlarmModel) -> Unit
) {
    val itemHeight = 76.dp
    SwipeToRevealOption(
        modifier = Modifier.fillMaxWidth(),
        content = {
            AlarmItem(
                alarm = alarm,
                onItemClick = onItemClick,
                onCheckedChange = onCheckedChange
            )
        },
        options = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .background(Color.Red)
                    .clickable { onDelete(alarm) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    color = Color.White,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                )
            }
        },
        optionWidth = 80.dp
    )
}

@Composable
private fun AlarmItem(
    alarm: AlarmModel,
    onItemClick: (AlarmModel) -> Unit,
    onCheckedChange: (AlarmModel) -> Unit
) {
    Column {
        AlarmItemView(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(alarm) }
                .padding(vertical = 24.dp, horizontal = 32.dp),
            item = alarm,
            onCheckedChange = {
                onCheckedChange(alarm)
            }
        )
        HorizontalDivider(
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
private fun AlarmItemView(
    modifier: Modifier = Modifier,
    item: AlarmModel,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val prefix = item.label
    val displayText = getAlarmRepeatDisplayText(item.repeatDays.orEmpty()).let {
        if (it.isBlank()) prefix else "$prefix, $it"
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.displayTime,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
            )
            if (item.label.isNullOrBlank().not()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = displayText.orEmpty(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
        Switch(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            checked = item.isEnabled,
            onCheckedChange = onCheckedChange,
        )
    }
}
