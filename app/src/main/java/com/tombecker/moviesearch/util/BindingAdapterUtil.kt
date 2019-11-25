package com.tombecker.moviesearch.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:glideImgViewUrl")
fun loadImage(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .override(200, 300)
        .placeholder(com.tombecker.moviesearch.R.drawable.ic_launcher_background)
        .into(view)
}