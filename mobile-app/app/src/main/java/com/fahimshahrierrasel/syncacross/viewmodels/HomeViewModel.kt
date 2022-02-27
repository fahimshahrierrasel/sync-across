package com.fahimshahrierrasel.syncacross.viewmodels

import com.fahimshahrierrasel.syncacross.config.FirebaseConfig
import com.fahimshahrierrasel.syncacross.data.models.SyncItem
import com.fahimshahrierrasel.syncacross.utils.logInAndroid
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class HomeViewState(
    val isLoading: Boolean = true,
    val message: String = "",
    val syncItems: MutableList<SyncItem> = mutableListOf()
)

sealed class HomeShotEvent {
    object Error : HomeShotEvent()
}

sealed class HomeUIAction {
    object GetSyncItems : HomeUIAction()
    object RefreshSyncItems : HomeUIAction()
    class NewSyncItem(val item: String) : HomeUIAction()
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
            is HomeUIAction.GetSyncItems -> {
                coroutineScope.launch {
                    try {
                        setLoadingState(true)
                        val response = FirebaseConfig.getSyncItems(lastItem = lastPage)
                        lastPage = response.lastData;
                        addSyncItems(response.data)
                    } catch (e: Exception) {
                        logInAndroid("Error: " + e.localizedMessage);
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
                        logInAndroid("Error: " + e.localizedMessage);
                    } finally {
                        setLoadingState(false)
                    }
                }
            }
            is HomeUIAction.NewSyncItem -> TODO()
        }
    }

    private fun setLoadingState(loadingState: Boolean) {
        _viewState.value = _viewState.value.copy(isLoading = loadingState)
    }

    private fun addSyncItems(syncItems: List<SyncItem>, clearExisting: Boolean = false) {
        if (clearExisting) {
            _viewState.value =
                _viewState.value.copy(
                    syncItems = mutableListOf(
                        *syncItems.toTypedArray()
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
}