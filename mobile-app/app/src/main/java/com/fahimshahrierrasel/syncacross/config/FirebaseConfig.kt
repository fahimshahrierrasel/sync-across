package com.fahimshahrierrasel.syncacross.config

import com.fahimshahrierrasel.syncacross.BuildConfig
import com.fahimshahrierrasel.syncacross.data.models.FirebaseResponse
import com.fahimshahrierrasel.syncacross.data.models.PageLimit
import com.fahimshahrierrasel.syncacross.data.models.SyncItem
import com.fahimshahrierrasel.syncacross.data.models.Tag
import com.fahimshahrierrasel.syncacross.utils.logInAndroid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

enum class FireStoreCollection(val value: String) {
    ItemCollection("messages"),
    TagCollection("tags")
}

object FirebaseConfig {
    val auth = FirebaseAuth.getInstance()

    init {
        if (BuildConfig.DEBUG) {
            logInAndroid("Setting Emulator Config ${BuildConfig.BUILD_TYPE} ${BuildConfig.DEBUG} ${BuildConfig.APPLICATION_ID}")
            Firebase.firestore.firestoreSettings = firestoreSettings {
                host = "10.0.2.2:8080"
                isSslEnabled = false
                isPersistenceEnabled = false
            }
            auth.useEmulator("10.0.2.2", 9099);
        }
    }

    suspend fun getSyncItems(
        limit: Long = PageLimit,
        lastItem: DocumentSnapshot? = null
    ): FirebaseResponse<SyncItem> {

        var query = Firebase.firestore.collection(FireStoreCollection.ItemCollection.value)
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

    suspend fun getTags(): List<Tag> {
        val query = Firebase.firestore.collection(FireStoreCollection.TagCollection.value)
            .orderBy("createdAt", Query.Direction.DESCENDING)
        val items = query.get().await()

        val tags = items.documents.mapNotNull { doc ->
            doc.toObject(Tag::class.java)?.apply {
                id = doc.id
            }
        }
        return tags
    }

    suspend fun createSyncItem(syncItem: SyncItem): SyncItem {
        val ref = Firebase.firestore.collection(FireStoreCollection.ItemCollection.value)
            .add(syncItem.toFirebaseObject())
            .await()

        return syncItem.apply {
            id = ref.id
        }
    }

    suspend fun createTag(tag: Tag): Tag {
        val ref = Firebase.firestore.collection(FireStoreCollection.TagCollection.value)
            .add(tag.toFirebaseObject())
            .await()
        return tag.apply {
            id = ref.id
        }
    }

    /**
     * Delete sync item from firebase
     *
     * Note: it will create some kind of issue in the app
     * when last item of the page removed and paginate next
     *
     * @param id the id of the sync item
     */
    suspend fun deleteSyncItem(id: String): Boolean {
        Firebase.firestore.collection(FireStoreCollection.ItemCollection.value)
            .document(id)
            .delete()
            .await()
        return true
    }

    suspend fun updateSyncItem(id: String, syncItem: SyncItem): Boolean {
        Firebase.firestore.collection(FireStoreCollection.ItemCollection.value)
            .document(id)
            .update(syncItem.toFirebaseObject())
            .await()
        return true
    }
}