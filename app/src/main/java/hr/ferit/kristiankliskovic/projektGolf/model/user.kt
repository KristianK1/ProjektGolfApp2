package hr.ferit.kristiankliskovic.projektGolf.model

data class user(
    var username: String,
    var password: String,
    var devices: ArrayList<Device>,
)
