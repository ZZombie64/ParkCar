package com.example.parkcar.model

import java.io.Serializable

class Place : Serializable{

    var id: Long
    val title: String
    var datewhen: String
    val description: String
    val latitude: Double
    val longitude: Double

    constructor(itemId: Long, itemName: String, itemDate: String, itemDescription: String, itemLat: Double, itemLon: Double){
        this.id = itemId
        this.title = itemName
        this.datewhen = itemDate
        this.description = itemDescription
        this.latitude = itemLat
        this.longitude = itemLon
    }

}