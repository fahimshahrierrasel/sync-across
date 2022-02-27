package com.fahimshahrierrasel.syncacross.data.models

import com.google.firebase.firestore.DocumentSnapshot

data class FirebaseResponse(val data: List<SyncItem>, val lastData: DocumentSnapshot) {
}