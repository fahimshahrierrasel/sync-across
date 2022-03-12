package com.fahimshahrierrasel.syncacross.data.models

import com.google.firebase.firestore.DocumentSnapshot

data class FirebaseResponse<T>(val data: List<T>, val lastData: DocumentSnapshot) {
}