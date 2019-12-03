package com.salim.maticcodingchallenge.util

import android.content.Context
import android.support.v4.widget.CircularProgressDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.salim.maticcodingchallenge.R
import java.text.SimpleDateFormat
import java.util.*

//create progress bar animation
fun getProgressDrawable(context : Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply{
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

//load image from url into image view with a progress bar view as place holder while loading
//sets default image if url fails to load
fun ImageView.loadImage(url:String?, progressDrawable: CircularProgressDrawable){
    val options:RequestOptions = RequestOptions()
            .placeholder(progressDrawable)
            .error(R.mipmap.ic_launcher_round)

    Glide.with(this.context)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(this)
}
//get fromatted date string as specified in pattern parameter
fun toSimpleString(date: Date,pattern:String) : String {
    val format = SimpleDateFormat(pattern)
    return format.format(date)
}

