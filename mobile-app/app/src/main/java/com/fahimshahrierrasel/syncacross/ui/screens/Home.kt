package com.fahimshahrierrasel.syncacross.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fahimshahrierrasel.syncacross.data.getDummyItems
import com.fahimshahrierrasel.syncacross.ui.components.*
import kotlinx.coroutines.launch

@Composable
fun Home(navController: NavController) {
    val items = getDummyItems(20);
    val selectableItems = listOf("All", "Bookmarks", "Medias", "Notes")
    val openSearch = remember { mutableStateOf(false) }
    val query = rememberSaveable { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (openSearch.value)
                SearchBar(
                    query = query.value,
                    onSearchClosed = {
                        openSearch.value = false
                    },
                    onQueryChanged = {
                        query.value = it
                    },
                ) else
                AppBar(
                    onSearchClicked = {
                        openSearch.value = true
                    },
                    onNavigationClicked = {
                        scope.launch {
                            scaffoldState.drawerState.open();
                        }
                    },
                )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*do something*/ }) {
                Icon(Icons.Default.Add, contentDescription = "Localized description")
            }
        },
        drawerContent = { AppDrawer(navController) }
    ) {
        Surface() {
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
                LazyColumn() {
                    items(items = items) { _ ->
                        SyncItemCard()
                    }
                }
            }
        }
    }
}