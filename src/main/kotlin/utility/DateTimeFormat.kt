package utility

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

fun formatTime(time: LocalTime): String {
    return formatTwoDigitValue(time.hour) + ":" + formatTwoDigitValue(time.minute)
}

fun formatTwoDigitValue(value: Int): String {
    if (value < 10) {
        return "0$value"
    }
    return value.toString()
}

fun formatMonthDay(date: LocalDate): String {
    return formatMonth(date.month) + " " + date.dayOfMonth
}

fun formatMonth(month: Month): String {
    val lower = month.toString().lowercase()
    return lower.first().uppercase() + lower.substring(1)
}
