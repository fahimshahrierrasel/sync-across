package com.fahimshahrierrasel.syncacross.data.models

import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.full.memberProperties

data class SyncItem(
    var title: String,
    var type: ItemType,
    var value: String,
    var origin: String,
    var tags: List<String>,
    var createdAt: Date
) {
    constructor() : this("Untitled", ItemType.MESSAGE, "", "", emptyList(), Date())

    fun toFirebaseObject(): HashMap<String, Any> {
        val firebaseObject = HashMap<String, Any>()
        this::class.memberProperties.forEach {
            firebaseObject[it.name] = it.getter.call(this)!!
        }
        return firebaseObject
    }
}
