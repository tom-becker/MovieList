package com.tombecker.moviesearch.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tombecker.moviesearch.api.ApiManager
import com.tombecker.moviesearch.model.MoviesResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieListViewModel: ViewModel() {

    val movieListLiveData = MutableLiveData<List<MoviesResponse.Movie>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val apiErrorLiveData = MutableLiveData<Boolean>()

    private val apiManager = ApiManager()
    private val disposable = CompositeDisposable()

    private var movieList = arrayListOf<MoviesResponse.Movie>()

    fun fetchMoviesByTitle(title: String) {
        loadingLiveData.postValue(true)

        disposable.add(
            apiManager.getMoviesByTitle(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    processMoviesResponse(response)
                }, { error ->
                    handleApiError(error)
                })
        )
    }

    fun fetchMoviesByTitleWithTypeFilter(title: String, type: String) {
        loadingLiveData.postValue(true)

        disposable.add(
            apiManager.getMoviesByTitleFilterType(title, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    processMoviesResponse(response)
                }, { error ->
                    handleApiError(error)
                })
        )
    }

    fun refreshCurrentList() {
        movieListLiveData.postValue(movieList)
    }

    private fun processMoviesResponse(moviesResponse: MoviesResponse) {
        loadingLiveData.postValue(false)

        movieList.clear()
        moviesResponse.let { response ->
            movieList.addAll(response.movies)
        }
        movieListLiveData.postValue(movieList)
    }

    private fun handleApiError(error: Throwable) {
        loadingLiveData.postValue(false)
        movieList.clear()
        movieListLiveData.postValue(movieList)
        Log.e("Movies API Error", error.localizedMessage)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}