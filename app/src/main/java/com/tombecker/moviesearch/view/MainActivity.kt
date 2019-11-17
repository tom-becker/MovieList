package com.tombecker.moviesearch.view

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tombecker.moviesearch.R
import com.tombecker.moviesearch.model.MoviesResponse.Movie
import com.tombecker.moviesearch.util.NetworkChangeReceiver
import com.tombecker.moviesearch.viewmodel.MovieListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_input_dialog.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieListViewModel
    private lateinit var movieListAdapter: MovieListAdapter

    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var emptyListTextView: TextView
    private lateinit var noInternetTextView: TextView
    private lateinit var searchMoviesFab: FloatingActionButton

    private val networkChangeReceiver = NetworkChangeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this)[MovieListViewModel::class.java]
        initView()
        subscribeToNetworkReceiver()
        subscribeToViewModel()
    }

    private fun initView() {
        movieListAdapter = MovieListAdapter(this)

        movieRecyclerView = movie_list_rv.apply {
            adapter = movieListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        searchMoviesFab = search_movies_button.also {
            it.setOnClickListener { promptUserInput() }
        }

        loadingProgressBar = loading_progress_bar
        emptyListTextView = no_data_tv
        noInternetTextView = no_internet_tv
    }

    private fun promptUserInput() {
        val builder = AlertDialog.Builder(this).apply {
            title = "Search Movies"
        }
        val dialogView = layoutInflater.inflate(R.layout.search_input_dialog, null)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            val searchText = dialogView.movie_name_edittext.text.toString()
            when(dialogView.radio_group_search_type.checkedRadioButtonId) {
                R.id.radio_all -> viewModel.fetchMoviesByTitle(searchText)
                R.id.radio_movie -> viewModel
                    .fetchMoviesByTitleWithTypeFilter(searchText, "movie")
                R.id.radio_series -> viewModel
                    .fetchMoviesByTitleWithTypeFilter(searchText, "series")
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }

        builder.setView(dialogView)

        builder.show()
    }

    private fun subscribeToNetworkReceiver() {
        //deprecated for usage if declared in Manifest
        //Still ok to use in this manner
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(networkChangeReceiver, filter)

        networkChangeReceiver.noNetworkLiveData
            .observe(this, Observer<Boolean> { isNetworkConnected ->
                showNetworkErrorText(isNetworkConnected)
        })
    }

    private fun showNetworkErrorText(isNetworkConnected: Boolean) {
        if(isNetworkConnected) {
            viewModel.refreshCurrentList()
            noInternetTextView.visibility = View.GONE
            searchMoviesFab.isEnabled = true
        } else {
            movieRecyclerView.visibility = View.GONE
            noInternetTextView.visibility = View.VISIBLE
            emptyListTextView.visibility = View.GONE
            searchMoviesFab.isEnabled = false
        }
    }

    private fun subscribeToViewModel() {
        viewModel.movieListLiveData
            .observe(this, Observer<List<Movie>> { movies ->
                handleNewList(movies)
                movieListAdapter.refreshList(movies)
        })

        viewModel.loadingLiveData.observe(this, Observer<Boolean> { bool ->
            loadingProgressBar.visibility = if (bool) View.VISIBLE else View.GONE
            emptyListTextView.visibility = if (bool) View.GONE else View.VISIBLE
        })

        viewModel.apiErrorLiveData.observe(this, Observer {
            handleNewList(listOf())
        })
    }

    private fun handleNewList(movies: List<Movie>) {
        if(movies.isEmpty()) {
            emptyListTextView.visibility = View.VISIBLE
            movieRecyclerView.visibility = View.GONE
        }
        else {
            emptyListTextView.visibility = View.GONE
            movieRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkChangeReceiver)
    }
}
