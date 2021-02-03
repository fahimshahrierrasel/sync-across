package com.fahimshahrierrasel.syncacross.utils

import android.content.Context
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity

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