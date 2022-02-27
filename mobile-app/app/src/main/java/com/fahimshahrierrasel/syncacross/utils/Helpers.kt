package com.fahimshahrierrasel.syncacross.utils

import android.content.Intent
import android.net.Uri
import android.util.Log

fun openInBrowser(url: String){
    val actionIntent = Intent(Intent.ACTION_VIEW)
    actionIntent.data = Uri.parse(url)
//    MainActivity.Instance.startActivity(actionIntent)
}

fun logInAndroid(message: String){
    Log.d("APP_LOG", message);
}