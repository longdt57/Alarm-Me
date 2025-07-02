package leegroup.module.alarm.ui.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import leegroup.module.alarm.R
import leegroup.module.alarm.ui.models.RepeatDay

@Composable
fun getAlarmRepeatDisplayText(
    repeatDays: List<Int>,
    default: String = "",
): String {
    val sortedRepeatDay = repeatDays.sorted()
    when {
        sortedRepeatDay.isEmpty() -> return default
        sortedRepeatDay == RepeatDay.entries.map { it.value } -> return stringResource(id = R.string.everyday)
        sortedRepeatDay == RepeatDay.getWeekdayValues() -> return stringResource(id = R.string.every_weekday)
        sortedRepeatDay == RepeatDay.getWeekendValues() -> return stringResource(id = R.string.every_weekend)
    }

    val dayResources = listOf(
        R.string.mon,
        R.string.tue,
        R.string.wed,
        R.string.thu,
        R.string.fri,
        R.string.sat,
        R.string.sun,
    ).map { stringResource(it) }
    return sortedRepeatDay.map { (it + 5) % 7 }.sorted().joinToString(", ") { dayResources[it % 7] }
}