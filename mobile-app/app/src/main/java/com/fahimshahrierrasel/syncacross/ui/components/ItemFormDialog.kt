package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fahimshahrierrasel.syncacross.data.models.ItemType
import com.fahimshahrierrasel.syncacross.data.models.SyncItem
import com.fahimshahrierrasel.syncacross.ui.localCapitalize
import com.fahimshahrierrasel.syncacross.viewmodels.HomeUIAction
import com.fahimshahrierrasel.syncacross.viewmodels.HomeViewModel
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ItemFormDialog(
    viewModel: HomeViewModel,
    onDismiss: () -> Unit,
    onSave: (item: SyncItem) -> Unit,
    syncItem: SyncItem? = null,
) {
    val itemTypes: List<RadioGroupItem> =
        ItemType.values().filter { it != ItemType.MEDIA }.map { RadioGroupItem(it.value, it.value) }
    val selectedType = remember { mutableStateOf(syncItem?.type ?: ItemType.BOOKMARK.value) }
    val title = remember { mutableStateOf(syncItem?.title ?: "") }
    val itemValue = remember { mutableStateOf(syncItem?.value ?: "") }
    val selectedTags: MutableList<String> =
        remember {
            if (syncItem != null)
                mutableStateListOf(*syncItem.tags!!.toTypedArray())
            else
                mutableStateListOf()
        }
    val viewState = viewModel.viewState.collectAsState()

    fun onFormSubmit() {
        if (title.value.length < 3) {
            viewModel.onAction(HomeUIAction.ShowMessage("Title is too short"))
            return
        }

        if (itemValue.value.length < 3) {
            viewModel.onAction(HomeUIAction.ShowMessage("Value is too short"))
            return
        }

        var item = SyncItem(
            id = "",
            title = title.value,
            type = selectedType.value,
            value = itemValue.value,
            origin = "",
            tags = selectedTags.toList(),
            createdAt = Date()
        )

        if (syncItem != null) {
            item = syncItem.copy(
                title = title.value,
                type = selectedType.value,
                value = itemValue.value,
                tags = selectedTags.toList(),
            )
        }

        onSave(item)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close")
                    }
                    Text(
                        "${if (syncItem != null) "Update" else "New"} Item",
                        fontSize = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { onFormSubmit() }) {
                        Text("Save")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    value = title.value,
                    onValueChange = { title.value = it },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Type")
                RadioGroup(
                    items = itemTypes,
                    selectedItem = selectedType.value,
                    onItemClick = {
                        selectedType.value = it
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    label = { Text(selectedType.value.localCapitalize()) },
                    value = itemValue.value,
                    onValueChange = { itemValue.value = it },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tags")
                MultiSelect(
                    modifier = Modifier.padding(vertical = 8.dp),
                    items = viewState.value.tags.map { it.title },
                    selectedItems = selectedTags,
                    onItemSelectionChanged = { tag ->
                        selectedTags.add(tag)
                    },
                    onItemDelete = { tag ->
                        selectedTags.remove(tag)
                    }
                )
            }
        }
    }
}

