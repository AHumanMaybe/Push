package com.example.push

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface callChat {
        @GET("thony")
        suspend fun getResponse(@Query("group") group : String): Response<chat>
}