package hr.ferit.kristiankliskovic.projektGolf.utils

import hr.ferit.kristiankliskovic.projektGolf.model.TSmainClass

interface TSdataFetched {
    fun onDataRecived(data: TSmainClass)
}