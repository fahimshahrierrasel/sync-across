package com.fahimshahrierrasel.syncacross.utils

import android.content.Context
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

fun AppCompatActivity.readBoolFromSharedPreference(key: String): Boolean {
    val sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    return sharedPref.getBoolean(key, false);
}

fun AppCompatActivity.writeBoolToSharedPreference(key: String, value: Boolean) {
    val sharedPref = this.getPreferences(Context.MODE_PRIVATE);
    with(sharedPref.edit()) {
        putBoolean(key, value)
        apply()
    }
}

fun String.getFileNameFromURL(): String {
    var name = this.substring(0, this.lastIndexOf('?'))
    name = name.split("/").last();
    name = name.substring(name.lastIndexOf("%2F") + 3)
    return name.replace("sync_files", "")
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Date.toFormatted(): String {
    //Aug 2, 2021, 11:54:23 PM
    val formatter = SimpleDateFormat("MMM d, yyyy, hh:mm:ss a", Locale.US)
    return formatter.format(this)
}