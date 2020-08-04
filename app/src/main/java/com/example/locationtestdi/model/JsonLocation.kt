package com.example.locationtestdi.model

class JsonLocation(
    val list: List<JsonLocationItem>

) {
    data class JsonLocationItem(
        val address: String,
        val id: String,
        val latitude: String,
        val longitude: String,
        val picture: String,
        val schedule: String,
        val state: String
    )
}