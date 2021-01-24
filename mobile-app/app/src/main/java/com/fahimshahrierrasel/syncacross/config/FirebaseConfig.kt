package com.fahimshahrierrasel.syncacross.config

import android.util.Log
import com.fahimshahrierrasel.syncacross.BuildConfig
import com.fahimshahrierrasel.syncacross.models.FireStoreCollection
import com.fahimshahrierrasel.syncacross.models.SyncItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

object FirebaseConfig {
    private val TAG = Firebase::class.simpleName
    private val db: FirebaseFirestore = Firebase.firestore
    private val storage: FirebaseStorage = Firebase.storage
    val syncFilesRef = storage.reference.child("sync_files")
    val auth = FirebaseAuth.getInstance()

    init {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Setting Emulator Config")
            db.firestoreSettings = firestoreSettings {
                host = "10.0.2.2:8080"
                isSslEnabled = false
                isPersistenceEnabled = false
            }
            auth.useEmulator("10.0.2.2", 9099);
        }
    }

    fun messageCollectionReference(): CollectionReference {
        return db.collection(FireStoreCollection.MessageCollection.value)
    }

    suspend fun createMessage(syncItem: SyncItem) {
        try {
            db.collection(FireStoreCollection.MessageCollection.value)
                .add(syncItem.toFirebaseObject())
                .await()
            Log.i(TAG, "Message Created")
        } catch (ex: FirebaseFirestoreException) {
            Log.e(TAG, ex.message, ex)
        }
    }

    suspend fun deleteMessage(id: String): Boolean {
        return try {
            db.collection(FireStoreCollection.MessageCollection.value)
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