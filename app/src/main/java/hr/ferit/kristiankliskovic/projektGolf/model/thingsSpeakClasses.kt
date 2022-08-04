package hr.ferit.kristiankliskovic.projektGolf.model

class TSmainClass {
    var channel: TSchannel = TSchannel()
    var feeds: Array<TSfeed> = emptyArray<TSfeed>()
}

class TSchannel {
    var id: Int = -1
    var name: String = ""
    var description: String = ""
    var latitude: String = ""
    var longitude: String = ""
    var field1: String = ""
    var field2: String = ""
    var created_at: String = ""
    var updated_at: String = ""
    var elevation: String = ""
    var last_entry_id: Int = -1
}

class TSfeed {
    var created_at: String = ""
    var entry_id: Int = -1
    var field1: String? = ""
    var field2: String? = ""
}