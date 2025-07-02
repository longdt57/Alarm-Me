package leegroup.module.alarm.ui.screens.setup.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import leegroup.module.alarm.R
import leegroup.module.alarm.ui.models.RepeatDay
import leegroup.module.alarm.ui.screens.components.PrimaryBottomSheet

@Composable
internal fun AlarmSetupRepeatBottomSheet(
    modifier: Modifier = Modifier,
    selectedItems: List<Int>,
    onItemClick: (Int) -> Unit = {},
    onDismissed: () -> Unit = {},
) {

    val options = listOf(
        AlarmSetupRepeat(stringResource(R.string.monday), RepeatDay.MONDAY.value),
        AlarmSetupRepeat(stringResource(R.string.tuesday), RepeatDay.TUESDAY.value),
        AlarmSetupRepeat(stringResource(R.string.wednesday), RepeatDay.WEDNESDAY.value),
        AlarmSetupRepeat(stringResource(R.string.thursday), RepeatDay.THURSDAY.value),
        AlarmSetupRepeat(stringResource(R.string.friday), RepeatDay.FRIDAY.value),
        AlarmSetupRepeat(stringResource(R.string.saturday), RepeatDay.SATURDAY.value),
        AlarmSetupRepeat(stringResource(R.string.sunday), RepeatDay.SUNDAY.value),
    )


    PrimaryBottomSheet(
        isVisible = true,
        modifier = modifier,
        onDismissed = onDismissed,
        dismissOnSelected = false,
        title = stringResource(R.string.repeat).uppercase(),
        onItemClick = { title ->
            onItemClick(options.first { it.title == title }.value)
        },
        options = options.map { it.title },
        selectedItems = options.filter { selectedItems.contains(it.value) }.map { it.title },
    )
}

data class AlarmSetupRepeat(val title: String, val value: Int)