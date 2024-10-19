package com.tadevi.demo.app.util

import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageLoaderUtil {
    fun loadCircleShape(imageView: ImageView, url: String) {
        Glide.with(imageView).load(url).circleCrop().into(imageView)
    }
}