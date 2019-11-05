package com.tombecker.moviesearch.api

import com.squareup.moshi.Moshi
import com.tombecker.moviesearch.model.MoviesResponse
import io.reactivex.Single
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


class ApiManager {

    private val baseUrl = HttpUrl.parse("https://www.omdbapi.com")
    private val moviesApiKey = "acce472a"

    private val moviesApi by lazy {
        val moshi = Moshi.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit.create(MoviesApi::class.java)
    }

    fun getMoviesByTitle(title: String): Single<MoviesResponse> {
        return moviesApi.getMoviesByTitle(moviesApiKey, title)
    }

    fun getMoviesByTitleFilterType(title: String, type: String): Single<MoviesResponse> {
        return moviesApi.getAllMoviesByType(moviesApiKey, title, type)
    }
}