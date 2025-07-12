package leegroup.module.alarm.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val AM = "AM"
private const val PM = "PM"
private val WHEEL_ITEM_HEIGHT = 36.dp // Height of each item in the wheel
private val WHEEL_ITEM_WIDTH = 60.dp // Height of each item in the wheel
private const val WHEEL_NUMBER_OF_ITEMS = 5
private val WHEEL_HEIGHT = WHEEL_ITEM_HEIGHT * WHEEL_NUMBER_OF_ITEMS

@Composable
internal fun WheelPicker(
    modifier: Modifier = Modifier
        .width(WHEEL_ITEM_WIDTH)
        .height(WHEEL_HEIGHT),
    state: LazyListState,
    items: List<String>,
    selectedItemCallback: (Int) -> Unit = {}
) {
    var previousIndex by remember { mutableIntStateOf(-1) }

    val visibleIndex by remember {
        derivedStateOf {
            val offset = state.firstVisibleItemScrollOffset
            val threshold = 18 // fine-tuned for 36.dp item
            val centeredIndex = state.firstVisibleItemIndex + if (offset > threshold) 1 else 0

            if (centeredIndex != previousIndex) {
                previousIndex = centeredIndex
                selectedItemCallback(centeredIndex)
            }

            centeredIndex
        }
    }

    LazyColumn(
        state = state,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = WHEEL_ITEM_HEIGHT * 2),
        flingBehavior = rememberSnapFlingBehavior(state),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items.size) { index ->
            val isSelected = index == visibleIndex
            Text(
                text = items[index],
                fontSize = if (isSelected) 24.sp else 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WHEEL_ITEM_HEIGHT)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}




@Composable
fun TimePickerWheel(
    modifier: Modifier = Modifier,
    initialTimePickerState: TimePickerState,
    onTimeSelected: (TimePickerState) -> Unit
) {
    val hours = (1..12).toList()
    val minutes = (0..59).toList()
    val periods = listOf(AM, PM)

    val infiniteMultiplier = 1000
    val infiniteHours = List(infiniteMultiplier * hours.size) { i ->
        hours[i % hours.size].toString()
    }
    val infiniteMinutes = List(infiniteMultiplier * minutes.size) { i ->
        minutes[i % minutes.size].toString().padStart(2, '0')
    }


    val hourState = rememberLazyListState((infiniteMultiplier / 2) * hours.size)
    val minuteState = rememberLazyListState((infiniteMultiplier / 2) * minutes.size)
    val periodState = rememberLazyListState()

    LaunchedEffect(initialTimePickerState) {
        val initialHourIndex =
            (infiniteMultiplier / 2) * hours.size + hours.indexOf(initialTimePickerState.hour)
        val initialMinuteIndex =
            (infiniteMultiplier / 2) * minutes.size + minutes.indexOf(initialTimePickerState.minute)

        launch {
            hourState.animateScrollToItem(initialHourIndex)
        }
        launch {
            minuteState.animateScrollToItem(initialMinuteIndex)
        }
        launch {
            periodState.animateScrollToItem(periods.indexOf(initialTimePickerState.period))
        }
    }

    Box(
        modifier = modifier.height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelPicker(state = hourState, items = infiniteHours)
            Spacer(modifier = Modifier.width(8.dp))
            WheelPicker(state = minuteState, items = infiniteMinutes)
            Spacer(modifier = Modifier.width(8.dp))
            WheelPicker(state = periodState, items = periods)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(WHEEL_ITEM_HEIGHT)
                .background(
                    Color.White.copy(0.1f), RoundedCornerShape(8.dp)
                )
        )
    }

    LaunchedEffect(Unit) {
        snapshotFlow {
            Triple(
                hourState.firstVisibleItemIndex + if (hourState.firstVisibleItemScrollOffset > 30) 1 else 0,
                minuteState.firstVisibleItemIndex + if (minuteState.firstVisibleItemScrollOffset > 30) 1 else 0,
                periodState.firstVisibleItemIndex + if (periodState.firstVisibleItemScrollOffset > 30) 1 else 0
            )
        }
            .distinctUntilChanged()
            .collect { (hIndex, mIndex, pIndex) ->
                val hour = hours.getOrNull(hIndex % hours.size) ?: initialTimePickerState.hour
                val minute =
                    minutes.getOrNull(mIndex % minutes.size) ?: initialTimePickerState.minute
                val period = periods.getOrNull(pIndex) ?: initialTimePickerState.period
                onTimeSelected(TimePickerState(hour, minute, period))
            }
    }

}

data class TimePickerState(
    val hour: Int = 0,
    val minute: Int = 0,
    val period: String = AM
) {
    companion object {

        fun parseTimeToPickerState(time: String): TimePickerState {
            return try {
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH)
                val parsedTime = LocalTime.parse(time, formatter)

                val hour24 = parsedTime.hour
                val minute = parsedTime.minute
                val period = if (hour24 < 12) AM else PM

                val hour12 = when {
                    hour24 == 0 -> 12
                    hour24 > 12 -> hour24 - 12
                    else -> hour24
                }

                TimePickerState(hour = hour12, minute = minute, period = period)
            } catch (e: Exception) {
                TimePickerState() // fallback: 12:00 AM
            }
        }
    }
}
