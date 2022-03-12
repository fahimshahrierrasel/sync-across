package com.fahimshahrierrasel.syncacross.data.models

class Constants {
    companion object {
        const val THEME_KEY = "THEME"
        const val URL_REGEX_PATTERN = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\-\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)"
    }
}

val imageExtensions = listOf("jpg", "jpeg", "png", "gif")
val URLRegex = Regex(Constants.URL_REGEX_PATTERN)
val PageLimit = 10L

