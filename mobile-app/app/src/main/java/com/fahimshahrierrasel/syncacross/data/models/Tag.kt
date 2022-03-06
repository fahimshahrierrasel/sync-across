package com.fahimshahrierrasel.syncacross.data.models

import java.util.*
import kotlin.reflect.full.memberProperties

data class Tag(var id: String, var title: String, var createdAt: Date) {
    constructor() : this("", "", Date())

    fun toFirebaseObject(): HashMap<String, Any> {
        val firebaseObject = HashMap<String, Any>()
        this::class.memberProperties.filter { it.name != "id" }.forEach {
            firebaseObject[it.name] = it.getter.call(this)!!
        }
        return firebaseObject
    }
}
