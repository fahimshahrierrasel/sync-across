package com.fahimshahrierrasel.syncacross.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fahimshahrierrasel.syncacross.utils.GlideApp
import java.util.*

@BindingAdapter("date")
fun bindDate(view: TextView, createdAt: Date) {
    val dateFormat = java.text.DateFormat.getDateTimeInstance()
    view.text = dateFormat.format(createdAt)
}

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(imageUrl: String){
    GlideApp.with(this.context)
        .load(imageUrl)
        .into(this)
}