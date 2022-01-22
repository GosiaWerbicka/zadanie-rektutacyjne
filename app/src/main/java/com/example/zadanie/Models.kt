package com.example.zadanie


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Models(
    @SerializedName("country")
    val country: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("confirmed")
    val confirmed: Int,
    @SerializedName("recovered")
    val recovered: Int,
    @SerializedName("critical")
    val critical: Int,
    @SerializedName("deaths")
    val deaths: Int,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("lastChange")
    val lastChange: String,
    @SerializedName("lastUpdate")
    val lastUpdate: String
)