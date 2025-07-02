package leegroup.module.alarm.support.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

internal object DateTimeUtil {

    const val HH_MM_SS = "HH:mm:ss"
    const val H_MM_A = "h:mm a"
    const val HH_MM = "HH:mm"

    /**
     * @param timeOfDay hh:mm:ss
     */
    fun toTimeInMinutes(timeOfDay: String): Int? {
        return try {
            val parts = timeOfDay.split(":")
            val hours = parts.getOrNull(0)?.toIntOrNull() ?: return null
            val minutes = parts.getOrNull(1)?.toIntOrNull() ?: return null
            hours * 60 + minutes
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentDayMinutes(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    }

    fun getCurrentAlarmTime(): String {
        val formatter = DateTimeFormatter.ofPattern(HH_MM_SS, Locale.ENGLISH)
        return LocalTime.now().format(formatter)
    }

    fun convertToAlarmDisplayFormat(time: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern(HH_MM_SS, Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern(H_MM_A, Locale.ENGLISH)
            val parsedTime = LocalTime.parse(time, inputFormatter)
            parsedTime.format(outputFormatter)
        } catch (e: Exception) {
            time // Return original if parsing fails
        }
    }

    fun convertToAlarmSaveTime(hour: Int, minute: Int, period: String): String {
        return try {
            val input = String.format(
                Locale.ENGLISH,
                "%d:%02d %s",
                hour,
                minute,
                period.uppercase(Locale.ENGLISH)
            )
            val inputFormatter = DateTimeFormatter.ofPattern(H_MM_A, Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern(HH_MM_SS, Locale.ENGLISH)
            val time = LocalTime.parse(input, inputFormatter)
            time.format(outputFormatter)
        } catch (e: Exception) {
            "00:00:00" // Fallback value
        }
    }
}
