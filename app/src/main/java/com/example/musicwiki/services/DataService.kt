package com.example.musicwiki.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataService {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://ws.audioscrobbler.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val lastFmService = retrofit.create(DataInterface::class.java)
}

