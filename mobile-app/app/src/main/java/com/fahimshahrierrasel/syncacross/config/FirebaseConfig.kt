package com.fahimshahrierrasel.syncacross.config

import android.util.Log
import com.fahimshahrierrasel.syncacross.data.models.FirebaseResponse
import com.fahimshahrierrasel.syncacross.data.models.SyncItem
import com.fahimshahrierrasel.syncacross.utils.logInAndroid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

enum class FireStoreCollection(val value: String) {
    MessageCollection("messages")
}

object FirebaseConfig {
    private val TAG = Firebase::class.simpleName
    private val storage: FirebaseStorage = Firebase.storage
    private val syncFilesRef = storage.reference.child("sync_files")
    val auth = FirebaseAuth.getInstance()

    init {
//        if (BuildConfig.DEBUG) {
//            Log.i(TAG, "Setting Emulator Config")
//            db.firestoreSettings = firestoreSettings {
//                host = "10.0.2.2:8080"
//                isSslEnabled = false
//                isPersistenceEnabled = false
//            }
//            auth.useEmulator("10.0.2.2", 9099);
//        }
    }

    suspend fun getSyncItems(
        limit: Long = 10,
        lastItem: DocumentSnapshot? = null
    ): FirebaseResponse {

        var query = Firebase.firestore.collection(FireStoreCollection.MessageCollection.value)
            .orderBy("createdAt", Query.Direction.DESCENDING)
        if (lastItem != null) {
            query = query.startAfter(lastItem)
        }
        val items = query.limit(limit).get().await()

        val syncItems = items.documents.mapNotNull { doc ->
            doc.toObject(SyncItem::class.java)?.apply {
                id = doc.id
            }
        }
        return FirebaseResponse(syncItems, items.documents[items.documents.size - 1])

    }

    suspend fun createMessage(syncItem: SyncItem) {
        try {
            Firebase.firestore.collection(FireStoreCollection.MessageCollection.value)
                .add(syncItem.toFirebaseObject())
                .await()
            Log.i(TAG, "Message Created")
        } catch (ex: FirebaseFirestoreException) {
            Log.e(TAG, ex.message, ex)
        }
    }

    suspend fun deleteMessage(id: String): Boolean {
        return try {
            Firebase.firestore.collection(FireStoreCollection.MessageCollection.value)
                .document(id)
                .delete()
                .await()
            Log.i(TAG, "Message Deleted")
            true
        } catch (ex: FirebaseFirestoreException) {
            Log.e(TAG, ex.message, ex)
            false
        }
    }

    suspend fun deleteFile(fileName: String): Boolean {
        return try {
            syncFilesRef.child(fileName).delete().await()
            true
        } catch (ex: StorageException) {
            Log.e(TAG, ex.message, ex)
            false
        }
    }
}