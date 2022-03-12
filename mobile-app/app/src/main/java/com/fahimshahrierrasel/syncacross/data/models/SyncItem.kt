package com.fahimshahrierrasel.syncacross.data.models

import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.full.memberProperties

data class SyncItem(
    var id: String,
    var title: String?,
    var type: String,
    var value: String,
    var origin: String,
    var tags: List<String>?,
    var createdAt: Date
) {
    constructor() : this("", "Untitled", ItemType.BOOKMARK.value, "", "", emptyList(), Date())

    constructor(title: String, value: String) : this(
        "",
        title,
        ItemType.BOOKMARK.value,
        value,
        "",
        emptyList(),
        Date()
    )

    fun toFirebaseObject(): HashMap<String, Any> {
        val firebaseObject = HashMap<String, Any>()
        this::class.memberProperties.filter { it.name != "id" }.forEach {
            firebaseObject[it.name] = it.getter.call(this)!!
        }
        return firebaseObject
    }
}
