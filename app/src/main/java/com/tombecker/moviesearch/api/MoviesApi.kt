package com.tombecker.moviesearch.api

import com.tombecker.moviesearch.model.MoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("/")
    fun getMoviesByTitle(
        @Query("apikey") apiKey: String,
        @Query("s") title: String): Single<MoviesResponse>

    @GET("/")
    fun getAllMoviesByType(
        @Query("apikey") apiKey: String,
        @Query("s") title: String,
        @Query("type") type: String): Single<MoviesResponse>

}