package com.example.push

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface api {

    @GET("leaderboard")
    suspend fun getItems(
        //@Query("api_key") apiKey: String

    ): Response<List<got>>
}