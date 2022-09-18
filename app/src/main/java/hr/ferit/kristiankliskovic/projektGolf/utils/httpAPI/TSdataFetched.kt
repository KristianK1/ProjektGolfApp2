package hr.ferit.kristiankliskovic.projektGolf.utils.httpAPI

import hr.ferit.kristiankliskovic.projektGolf.model.TSmainClass

interface TSdataFetched {
    fun onDataRecived(data: TSmainClass)
}