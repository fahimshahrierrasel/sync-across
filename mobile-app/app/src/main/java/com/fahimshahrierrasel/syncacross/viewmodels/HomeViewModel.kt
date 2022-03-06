package com.fahimshahrierrasel.syncacross.viewmodels

import android.os.Build
import com.fahimshahrierrasel.syncacross.config.FirebaseConfig
import com.fahimshahrierrasel.syncacross.data.models.SyncItem
import com.fahimshahrierrasel.syncacross.data.models.Tag
import com.fahimshahrierrasel.syncacross.utils.logInAndroid
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

data class HomeViewState(
    val isLoading: Boolean = true,
    val message: String = "",
    val syncItems: MutableList<SyncItem> = mutableListOf(),
    val tags: MutableList<Tag> = mutableListOf()
)

sealed class HomeShotEvent {
    object Error : HomeShotEvent()
}

sealed class HomeUIAction {
    object GetTags : HomeUIAction()
    object GetSyncItems : HomeUIAction()
    object RefreshSyncItems : HomeUIAction()
    class NewSyncItem(val item: SyncItem) : HomeUIAction()
    class DeleteSyncItem(val id: String) : HomeUIAction()
    class UpdateSyncItem(val item: SyncItem) : HomeUIAction()
    class NewTag(val title: String) : HomeUIAction()
    class ShowMessage(val message: String) : HomeUIAction()
}

class HomeViewModel {
    private val coroutineScope = MainScope()
    private val _viewState: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState())
    private val _shotEvents = Channel<HomeShotEvent>(Channel.BUFFERED)
    private var lastPage: DocumentSnapshot? = null

    val viewState = _viewState.asStateFlow()
    val shotEvents = _shotEvents.receiveAsFlow()

    fun onAction(uiAction: HomeUIAction) {
        when (uiAction) {
            is HomeUIAction.ShowMessage -> {
                coroutineScope.launch {
                    setMessage(uiAction.message)
                }
            }
            is HomeUIAction.GetTags -> {
                coroutineScope.launch {
                    try {
                        setLoadingState(true)
                        val tags = FirebaseConfig.getTags()
                        addTags(tags)
                    } catch (e: Exception) {
                        logInAndroid("Error: " + e.localizedMessage)
                    } finally {
                        setLoadingState(false)
                    }
                }
            }
            is HomeUIAction.GetSyncItems -> {
                coroutineScope.launch {
                    try {
                        setLoadingState(true)
                        val response = FirebaseConfig.getSyncItems(lastItem = lastPage)
                        lastPage = response.lastData
                        addSyncItems(response.data)
                    } catch (e: Exception) {
                        logInAndroid("Error: " + e.localizedMessage)
                    } finally {
                        setLoadingState(false)
                    }
                }
            }
            is HomeUIAction.RefreshSyncItems -> {
                coroutineScope.launch {
                    try {
                        setLoadingState(true)
                        val response = FirebaseConfig.getSyncItems()
                        lastPage = response.lastData
                        addSyncItems(response.data, clearExisting = true)
                    } catch (e: Exception) {
                        logInAndroid("Error: " + e.localizedMessage)
                    } finally {
                        setLoadingState(false)
                    }
                }
            }
            is HomeUIAction.NewSyncItem -> {
                val item = uiAction.item.apply {
                    origin = Build.MODEL
                    createdAt = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
                }
                coroutineScope.launch {
                    try {
                        setLoadingState(true)
                        val syncItem = FirebaseConfig.createSyncItem(item)
                        addSyncItems(listOf(syncItem), isNewItem = true)
                    } catch (e: Exception) {
                        logInAndroid("Error: " + e.localizedMessage)
                    } finally {
                        setLoadingState(false)
                    }
                }
            }
            is HomeUIAction.NewTag -> {
                val tag = Tag(
                    id = "",
                    title = uiAction.title,
                    createdAt = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
                )

                coroutineScope.launch {
                    try {
                        setLoadingState(true)
                        val newTag = FirebaseConfig.createTag(tag)
                        addTags(listOf(newTag))
                    } catch (e: Exception) {
                        logInAndroid("Error: " + e.localizedMessage)
                    } finally {
                        setLoadingState(false)
                    }
                }
            }
            is HomeUIAction.DeleteSyncItem -> {
                coroutineScope.launch {
                    try {
                        setLoadingState(true)
                        val isRemoved = FirebaseConfig.deleteSyncItem(uiAction.id)
                        if (isRemoved) {
                            removeSyncItem(uiAction.id)
                        }
                    } catch (e: Exception) {
                        logInAndroid("Error: " + e.localizedMessage)
                    } finally {
                        setLoadingState(false)
                    }
                }
            }
            is HomeUIAction.UpdateSyncItem -> {
                coroutineScope.launch {
                    try {
                        setLoadingState(true)
                        val isUpdated = FirebaseConfig.updateSyncItem(uiAction.item.id, uiAction.item)
                        if(isUpdated){
                            updateSyncItem(uiAction.item);
                        }
                    } catch (e: Exception) {
                        logInAndroid("Error: " + e.localizedMessage)
                    } finally {
                        setLoadingState(false)
                    }
                }
            }
        }
    }

    private fun setLoadingState(loadingState: Boolean) {
        _viewState.value = _viewState.value.copy(isLoading = loadingState)
    }

    private fun addSyncItems(
        syncItems: List<SyncItem>,
        clearExisting: Boolean = false,
        isNewItem: Boolean = false
    ) {
        if (clearExisting) {
            _viewState.value =
                _viewState.value.copy(
                    syncItems = mutableListOf(
                        *syncItems.toTypedArray()
                    )
                )
            return
        }
        if (isNewItem) {
            _viewState.value =
                _viewState.value.copy(
                    syncItems = mutableListOf(
                        *syncItems.toTypedArray(),
                        *_viewState.value.syncItems.toTypedArray()
                    )
                )
            return
        }
        _viewState.value =
            _viewState.value.copy(
                syncItems = mutableListOf(
                    *_viewState.value.syncItems.toTypedArray(),
                    *syncItems.toTypedArray()
                )
            )
    }

    private fun removeSyncItem(id: String) {
        val syncItems = _viewState.value.syncItems.filter { it.id != id }
        addSyncItems(syncItems, clearExisting = true)
    }

    private fun updateSyncItem(item: SyncItem) {
        val syncItems = _viewState.value.syncItems.map {
            if (it.id == item.id) item else it
        }
        addSyncItems(syncItems, clearExisting = true)
    }

    private fun addTags(tags: List<Tag>) {
        _viewState.value = _viewState.value.copy(
            tags = mutableListOf(
                *_viewState.value.tags.toTypedArray(),
                *tags.toTypedArray()
            )
        )
    }

    private suspend fun setMessage(message: String) {
        _viewState.value = _viewState.value.copy(message = message)
        _shotEvents.send(HomeShotEvent.Error)
    }
}