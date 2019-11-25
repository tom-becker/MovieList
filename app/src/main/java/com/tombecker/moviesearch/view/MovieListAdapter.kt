package com.tombecker.moviesearch.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tombecker.moviesearch.databinding.MovieListItemBinding
import com.tombecker.moviesearch.model.MoviesResponse.Movie


class MovieListAdapter(private val context: Context): RecyclerView.Adapter<ViewHolder>() {

    private lateinit var viewHolder: ViewHolder

    private var movieList = arrayListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : MovieListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            com.tombecker.moviesearch.R.layout.movie_list_item,
            parent, false)

        viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    fun refreshList(movies: List<Movie>) {
        movieList.clear()
        movieList.addAll(movies)
        notifyDataSetChanged()
    }

}

class ViewHolder (binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    private val movieBinding = binding

    fun bind(m :Movie) {
        movieBinding.movie = m
    }

}