package hr.ferit.kristiankliskovic.projektGolf.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


fun CalendarToIso(c: Calendar): String {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(c.time)
}

fun ISOtoCalendar(iso: String): Calendar {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val date = sdf.parse(iso)
    val cal = java.util.Calendar.getInstance()
    cal.time = date
    return cal
}


fun CalendarToTSinputFormat(c: Calendar): String {
    return SimpleDateFormat("yyyy-MM-dd'%20'HH:mm:ss").format(c.time)
}


fun ISOtoTSinputFormat(iso: String): String {
    return CalendarToTSinputFormat(ISOtoCalendar(iso))
}

fun UnixToCalendar(unixTime: Long): Calendar? {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = unixTime * 1000
    return calendar
}

fun CalendarToUnix(cal: Calendar): Long {
    val timeInSec = cal.timeInMillis / 1000
    return timeInSec
}

fun isostring_toLocalCalendar(iso: String): Calendar {
    val UNIX = CalendarToUnix(ISOtoCalendar(iso))
//    Log.i("dayLight", "unix2 = " + UNIX)

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.JAPAN)

    val formattedDate: String = sdf.format(UNIX * 1000)
//    Log.i("dayLight", formattedDate)
    var result: Calendar = Calendar.getInstance()
    try {
        val yyyy = formattedDate.subSequence(0, 4).toString().toInt()
        val MM = formattedDate.subSequence(5, 7).toString().toInt()
        val dd = formattedDate.subSequence(8, 10).toString().toInt()

        val HH = formattedDate.subSequence(11, 13).toString().toInt()
        val mm = formattedDate.subSequence(14, 16).toString().toInt()
        val ss = formattedDate.subSequence(17, 19).toString().toInt()

        val offsetHours = formattedDate.subSequence(20, 22).toString().toInt()
        val offsetMinutes = formattedDate.subSequence(22, 24).toString().toInt()

        Log.i("dayLight", "year: $yyyy $MM $dd || $HH $mm $ss || $offsetHours $offsetMinutes")

        result = Calendar.getInstance().apply {
            set(Calendar.YEAR, yyyy)
            set(Calendar.MONTH, MM - 1)
            set(Calendar.DAY_OF_MONTH, dd)
            set(Calendar.HOUR_OF_DAY, HH + offsetHours)
            set(Calendar.MINUTE, mm + offsetMinutes)
            set(Calendar.SECOND, ss)
        }

//        Log.i("dayLight", "transformRez " + CalendarToIso(result))

    } catch (e: Throwable) {
        Log.i("dayLight", "error")
    }
    return result

}

fun CalendartoMapMarkerString(cal: Calendar): String {
    val ispis: String
    ispis =
        "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}:${cal.get(Calendar.SECOND)} ${
            cal.get(Calendar.DAY_OF_MONTH)
        }.${cal.get(Calendar.MONTH) + 1}.${cal.get(Calendar.YEAR)}"
    return ispis
}

fun addTimeToCalendar(
    calendarIn: Calendar,
    years: Int,
    months: Int,
    days: Int,
    hours: Int,
    mins: Int,
    seconds: Int
): Calendar {
    val calOut = Calendar.getInstance().apply {
        set(Calendar.YEAR, calendarIn.get(Calendar.YEAR) + years)
        set(Calendar.MONTH, calendarIn.get(Calendar.MONTH) + months)
        set(Calendar.DAY_OF_MONTH, calendarIn.get(Calendar.DAY_OF_MONTH) + days)
        set(Calendar.HOUR_OF_DAY, calendarIn.get(Calendar.HOUR_OF_DAY) + hours)
        set(Calendar.MINUTE, calendarIn.get(Calendar.MINUTE) + mins)
        set(Calendar.SECOND, calendarIn.get(Calendar.SECOND) + seconds)
    }
    return calOut
}






























