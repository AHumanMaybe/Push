package com.example.push

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface taskadd {
    @GET("inserttask")
    suspend fun getconfirm(@Query("uid") uid : String, @Query("score") score : Int): Response<confirm>
}