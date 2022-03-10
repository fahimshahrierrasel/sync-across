package com.fahimshahrierrasel.syncacross.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahimshahrierrasel.syncacross.data.models.SyncItem
import com.fahimshahrierrasel.syncacross.ui.OnLastItemReached
import com.fahimshahrierrasel.syncacross.ui.components.*
import com.fahimshahrierrasel.syncacross.ui.theme.AccentColor
import com.fahimshahrierrasel.syncacross.viewmodels.HomeShotEvent
import com.fahimshahrierrasel.syncacross.viewmodels.HomeUIAction
import com.fahimshahrierrasel.syncacross.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun Home(viewModel: HomeViewModel) {
    val selectableItems = listOf("All", "Bookmarks", "Medias", "Notes")
    val openSearch = remember { mutableStateOf(false) }
    val query = rememberSaveable { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewState = viewModel.viewState.collectAsState()
    val listState = rememberLazyListState()
    val isFormDialogOpened = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val selectedItem: MutableState<SyncItem?> = remember { mutableStateOf(null) }

    LaunchedEffect("key") {
        viewModel.shotEvents.onEach {
            when (it) {
                HomeShotEvent.Error -> {
//                    scaffoldState.snackbarHostState.showSnackbar(viewState.value.message)
                    Toast.makeText(context, viewState.value.message, Toast.LENGTH_SHORT).show()
                }
            }
        }.collect()

        viewModel.onAction(HomeUIAction.GetSyncItems)
        viewModel.onAction(HomeUIAction.GetTags)
    }

    listState.OnLastItemReached {
        viewModel.onAction(HomeUIAction.GetSyncItems)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (openSearch.value) {
                SearchBar(
                    query = query.value,
                    onSearchClosed = {
                        openSearch.value = false
                    },
                    onQueryChanged = {
                        query.value = it
                    },
                )
            } else {
                AppBar(
                    onSearchClicked = {
                        openSearch.value = true
                    },
                    onNavigationClicked = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    onRefreshClicked = {
                        viewModel.onAction(HomeUIAction.RefreshSyncItems)
                    }
                )
            }
        },
        floatingActionButton = {
            if (!viewState.value.isLoading) {
                FloatingActionButton(onClick = {
                    selectedItem.value = null
                    isFormDialogOpened.value = true
                }) {
                    Icon(Icons.Rounded.Add, contentDescription = "Localized description")
                }
            }
        },
        drawerContent = { AppDrawer(viewModel) }
    ) {
        Box {
            Surface {
                Column(modifier = Modifier.fillMaxWidth()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(items = selectableItems) { item ->
                            Tag(
                                item,
                                fontSize = 14.sp,
                                textPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                    LazyColumn(state = listState) {
                        items(items = viewState.value.syncItems.filter { st ->
                            (st.title != null && st.title!!.lowercase()
                                .contains(
                                    query.value.lowercase(),
                                    ignoreCase = true
                                )) || st.value.lowercase()
                                .contains(query.value.lowercase(), ignoreCase = true)
                        }, key = { i -> i.id }) { syncItem ->
                            SyncItemCard(
                                syncItem,
                                onUpdateClicked = {
                                    selectedItem.value = syncItem
                                    isFormDialogOpened.value = true
                                },
                                onDeleteClicked = {
                                    viewModel.onAction(HomeUIAction.DeleteSyncItem(syncItem.id))
                                },
                            )
                        }
                    }
                }
            }
            if (viewState.value.isLoading) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black.copy(alpha = 0.4f)) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = AccentColor)
                    }
                }
            }
            if (isFormDialogOpened.value) {
                ItemFormDialog(
                    viewModel = viewModel,
                    syncItem = selectedItem.value,
                    onDismiss = { isFormDialogOpened.value = false },
                    onSave = { syncItem ->
                        if (syncItem.id.isEmpty()) {
                            viewModel.onAction(HomeUIAction.NewSyncItem(syncItem))
                        } else {
                            viewModel.onAction(HomeUIAction.UpdateSyncItem(syncItem))
                        }
                        isFormDialogOpened.value = false
                    }
                )
            }
        }
    }
}