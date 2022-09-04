package hr.ferit.kristiankliskovic.projektGolf.utils

fun historyStringToSeconds(his: String): Int{
    val rez: Int
    when(his){
        "10 min" -> rez = 60*10
        "20 min" -> rez = 60 * 20
        "30 min" -> rez = 60 * 30
        "1 hour" -> rez = 60 * 60
        "2 hours" -> rez = 60 * 60 * 2
        "3 hours" -> rez = 60 * 60 * 3
        "6 hours" -> rez = 60 * 60 * 6
        "12 hours" -> rez = 60 * 60 * 12
        "1 day" -> rez = 60 * 60 * 24
        "2 days" -> rez = 60 * 60 * 24 * 2
        "5 days" -> rez = 60 * 60 * 24 * 5
        "7 days" -> rez = 60 * 60 * 24 * 7
        "10 days" -> rez = 60 * 60 * 24 * 10
        "14 days" -> rez = 60 * 60 * 24 * 14
        "30 days" -> rez = 60 * 60 * 24 * 30
        "60 days" -> rez = 60 * 60 * 24 * 60
        "90 days" -> rez = 60 * 60 * 24 * 90
        "120 days" -> rez = 60 * 60 * 24 * 120
        "180 days" -> rez = 60 * 60 * 24 * 180
        else -> rez = 0
    }
    return rez
}