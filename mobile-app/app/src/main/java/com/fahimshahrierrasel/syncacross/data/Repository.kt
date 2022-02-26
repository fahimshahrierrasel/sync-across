package com.fahimshahrierrasel.syncacross.data

fun getDummyItems(count: Int = 10): List<String> {
    return List(count) {
        "Item $it"
    }
}