package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fahimshahrierrasel.syncacross.R

@Composable
fun AppBar(onNavigationClicked: () -> Unit, onSearchClicked: () -> Unit, onRefreshClicked: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = onNavigationClicked) {
                Icon(
                    Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.menu)
                )
            }
        },
        actions = {
            IconButton(onClick = onRefreshClicked) {
                Icon(Icons.Rounded.Refresh, contentDescription = stringResource(R.string.refresh))
            }
            IconButton(onClick = onSearchClicked) {
                Icon(Icons.Rounded.Search, contentDescription = stringResource(R.string.search))
            }
        },
    )
}