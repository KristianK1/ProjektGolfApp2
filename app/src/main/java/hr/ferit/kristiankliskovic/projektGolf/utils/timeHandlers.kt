package hr.ferit.kristiankliskovic.projektGolf.utils

import java.text.SimpleDateFormat
import java.util.*


fun CalendarToIso(c: Calendar): String{
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(c.time)
}

fun ISOtoCalendar(iso: String): Calendar{
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val date = sdf.parse(iso)
    val cal = java.util.Calendar.getInstance()
    cal.time = date
    return cal
}


fun CalendarToTSinputFormat(c: Calendar): String{
    return SimpleDateFormat("yyyy-MM-dd'%20'HH:mm:ss").format(c.time)
}


fun ISOtoTSinputFormat(iso: String): String{
    return CalendarToTSinputFormat(ISOtoCalendar(iso))
}