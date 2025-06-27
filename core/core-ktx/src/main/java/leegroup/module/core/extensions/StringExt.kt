package leegroup.module.core.extensions

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun String.hexToColor(): Color {
    return Color(toColorInt())
}

@Suppress("MagicNumber")
fun String?.appVersionToInt(): Int {
    fun Int?.orZero() = this ?: 0

    return this?.split(".")?.mapIndexed { index, value ->
        when (index) {
            0 -> value.toIntOrNull().orZero() * 1000
            1 -> value.toIntOrNull().orZero() * 100
            else -> value.toIntOrNull().orZero()
        }
    }.orEmpty().sumOf { it }
}