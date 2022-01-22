package com.example.zadanie

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Jsonapi {

    //https://covid-19-data.p.rapidapi.com/country


    @Headers(
        "x-rapidapi-host: covid-19-data.p.rapidapi.com",
        "x-rapidapi-key: 323b976cc6mshefea27dc6620aa2p1edbcejsn7fbfe4aea4e6"
    )
    @GET("country")
    fun getInfo(@Query("name") name: String = "italy"): Call<List<Models>>

}