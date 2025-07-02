package leegroup.module.alarm.ui.models

enum class RepeatDay(val value: Int) {
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7);

    companion object {
        fun fromValue(value: Int): RepeatDay? {
            return entries.find { it.value == value }
        }

        fun getWeekdayValues(): List<Int> {
            return entries.filter { it.value in MONDAY.value..FRIDAY.value }.map { it.value }
        }

        fun getWeekendValues(): List<Int> {
            return entries.filter {
                it.value in listOf(
                    SATURDAY.value, SUNDAY.value
                )
            }.map { it.value }
        }
    }
}