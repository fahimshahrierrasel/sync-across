package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.fahimshahrierrasel.syncacross.R
import com.fahimshahrierrasel.syncacross.ui.theme.PrimaryColor

@Composable
fun SearchBar(
    query: String,
    onSearchClosed: () -> Unit,
    onQueryChanged: (updatedQuery: String) -> Unit
) {
    Surface(color = PrimaryColor) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier.weight(1f),
                value = query,
                onValueChange = onQueryChanged,
                placeholder = { Text(stringResource(id = R.string.search_placeholder)) },
                leadingIcon = {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = stringResource(id = R.string.search),
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = PrimaryColor,
                    textColor = Color.White,
                    cursorColor = Color.White,
                    placeholderColor = Color.White.copy(alpha = 0.5f)
                )
            )
            IconButton(onClick = onSearchClosed) {
                Icon(Icons.Rounded.Close, contentDescription = "Close")
            }
        }
    }
}