package com.duongnh.shapetest.data.data_source.remote.api

import com.duongnh.shapetest.data.data_source.remote.dto.ColourResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ColourApi {

    @GET("colors/random")
    suspend fun getRandomColor(@Query("format") format: String): Response<List<ColourResponse>>

    @GET("patterns/random")
    suspend fun getRandomImageUrl(@Query("format") format: String): Response<List<ColourResponse>>

}