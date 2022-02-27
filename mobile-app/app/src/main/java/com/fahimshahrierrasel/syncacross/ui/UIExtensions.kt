package com.fahimshahrierrasel.syncacross.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*

@Composable
fun LazyListState.OnLastItemReached(
    onLoadMore: () -> Unit
) {
    val isOnBottom = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(isOnBottom) {
        snapshotFlow { isOnBottom.value }
            .collect {
                if (it) onLoadMore()
            }
    }
}