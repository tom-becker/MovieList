package com.tombecker.moviesearch.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.tombecker.moviesearch.R
import com.tombecker.moviesearch.model.Movie
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieListAdapter(private val context: Context):
    androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

    private lateinit var viewHolder: ViewHolder

    private var movieList = arrayListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false))
        return viewHolder
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder.apply {
            titleTextView.text = movieList[position].title
            typeTextView.text = movieList[position].type
            yearTextView.text = movieList[position].year
            Glide.with(context)
                .load(movieList[position].posterUrl)
                .override(200, 300)
                .placeholder(R.drawable.ic_launcher_background)
                .into(posterImgView)
        }
    }

    fun refreshList(movies: List<Movie>) {
        movieList.clear()
        movieList.addAll(movies)
        notifyDataSetChanged()
    }

}

class ViewHolder (view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val titleTextView: TextView = view.title_tv
    val typeTextView: TextView = view.type_tv
    val yearTextView: TextView = view.rating_tv
    val posterImgView: ImageView = view.movie_img
}