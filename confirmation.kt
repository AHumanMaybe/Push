package com.example.push

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.OPTIONS
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface confirmation {
    @GET("joingroup")
    suspend fun getconfirm(@Query("uid") uid : String, @Query("group") group : String): Response<confirm>
}