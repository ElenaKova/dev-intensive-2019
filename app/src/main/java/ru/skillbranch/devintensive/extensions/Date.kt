package ru.skillbranch.devintensive.extensions

import android.util.Log
import java.lang.Math.abs
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

//enum class TimeUnits { SECOND, MINUTE, HOUR, DAY,  }


fun String.toDate(pattern: String = "HH:mm:ss dd.MM.yy"): Date? = try {
    SimpleDateFormat(pattern, Locale("ru", "RU")).parse(this)
} catch (e: ParseException) {
    Log.e("DEV", "${javaClass.simpleName} toDate: $e")
    null
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = (date.time - this.time)

    return when (Math.abs(diff / DAY).toInt()) {

        0 -> {
            when (Math.abs(diff / HOUR).toInt()) {
                0 -> {
                    when (Math.abs(diff / MINUTE).toInt()) {
                        0 -> {
                            when (Math.abs(diff / SECOND).toInt()) {
                                in 0..15 -> if ((diff / SECOND).toInt() >= 0) "только что" else "скоро"
                                else -> if ((diff / SECOND).toInt() > 0) "менее минуты назад" else "более чем через минуту"
                            }
                        }
                        1 -> "минуту назад"
                        in 2..4, in 22..24,
                        in 32..34, in 42..44,
                        in 52..54 -> if ((diff / MINUTE).toInt() > 0) "${diff / MINUTE} минуты назад" else "через ${-diff / MINUTE} минуты"
                        21, 31, 41, 51 -> if ((diff / MINUTE).toInt() > 0) "${diff / MINUTE} минуту назад" else "через ${-diff / MINUTE} минуту"
                        else -> if ((diff / MINUTE).toInt() > 0) "${diff / MINUTE} минут назад" else "через ${-diff / MINUTE} минут"
                    }
                }
                1, 21 -> if ((diff / HOUR).toInt() > 0) "${diff / HOUR} час назад" else "через ${diff / HOUR} час"
                in 2..4, in 22..24 -> if ((diff / HOUR).toInt() > 0) "${diff / HOUR} часа назад" else "через ${diff / HOUR} часа"
                else -> if ((diff / HOUR).toInt() > 0) "${diff / HOUR} часов назад" else "через ${diff / HOUR} часов"
            }
        }

        1 -> if ((diff / DAY).toInt() > 0) "вчера" else "завтра"
        in 2..4 -> if ((diff / DAY).toInt() > 0) "${diff / DAY} дня назад" else "через ${-diff / DAY} дня"
        in 5..7 -> if ((diff / DAY).toInt() > 0) "${diff / DAY} дней назад" else "через ${-diff / DAY} дней"
        in 8..14 -> if((diff / DAY).toInt() > 0) "более недели назад" else "более чем через неделю"
        in 15..31 -> if((diff / DAY).toInt() > 0) "более двух недель назад" else "более чем через две недели"
        in 32..61 -> if((diff / DAY).toInt() > 0) "более месяца назад" else "более чем через месяц"
        in 62..182 -> if((diff / DAY).toInt() > 0) "более ${diff / DAY} месяцев назад" else "более чем через ${-diff / DAY} месяца"
        in 183..365 -> if((diff / DAY).toInt() > 0) "более полугода назад" else "более чем через полгода"
        in 365..Int.MAX_VALUE -> if((diff / DAY).toInt() > 0) "более года назад" else "более чем через год"

        else -> "никогда"
    }
}

operator fun Date.minus(anoterDate:Date):Long = this.time - anoterDate.time

operator fun Date.plusAssign(interval: Long) {
    this.time += interval
}

operator fun Date.minusAssign(interval: Long) {
    this.time -= interval
}

fun Date.add(value:Long, units: TimeUnits):Date {
    this += units * value
    return this
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    operator fun times(value: Long):Long {
        return value * when(this) {
            SECOND -> 1000L
            MINUTE -> SECOND * 60
            HOUR -> MINUTE * 60
            DAY -> HOUR * 24
        }
    }

    fun plural(value:Long):String {
        return "${abs(value)} " + when (this) {
            SECOND -> {when (pluralForm(value)) {
                0 -> "секунду"
                1 -> "секунды"
                else -> "секунд"
            }
            }
            MINUTE -> {when (pluralForm(value)) {
                0 -> "минуту"
                1 -> "минуты"
                else -> "минут"
            }
            }
            HOUR -> {when (pluralForm(value)) {
                0 -> "час"
                1 -> "часа"
                else -> "часов"
            }
            }
            DAY -> {when (pluralForm(value)) {
                0 -> "день"
                1 -> "дня"
                else -> "дней"
            }
            }
        }
    }

    private fun pluralForm(value:Long):Int {
        val absvalue = abs(value)
        return if (absvalue%10==1L && absvalue%100!=11L) 0
        else if (absvalue%10>=2L && absvalue%10<=4L && (absvalue%100<10L || absvalue%100>=20L)) 1
        else 2
    }
}
