package com.example.hhc_groupnavi.data

data class LocationData(
    var latitude: Double? = null,
    var longitude: Double? = null,
) {
    fun toMap() = mapOf(
        "latitude" to latitude,
        "longitude" to longitude,
    )
}