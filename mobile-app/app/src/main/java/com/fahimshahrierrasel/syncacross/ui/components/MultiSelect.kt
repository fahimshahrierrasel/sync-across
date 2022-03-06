package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahimshahrierrasel.syncacross.utils.logInAndroid
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun MultiSelect(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedItems: MutableList<String>,
    onItemSelectionChanged: (item: String) -> Unit,
    onItemDelete: (item: String) -> Unit
) {
    val isDropdownExpanded = remember { mutableStateOf(false) }
    FlowRow(modifier = modifier, mainAxisSpacing = 4.dp, crossAxisSpacing = 4.dp) {
        selectedItems.forEach {
            Tag(it, onDelete = { onItemDelete(it) })
        }
        SmallButton(onClick = { isDropdownExpanded.value = true }) {
            Text("Select", fontSize = 12.sp)
            Icon(Icons.Rounded.ExpandMore, contentDescription = "Select")
        }
        DropdownMenu(
            expanded = isDropdownExpanded.value,
            onDismissRequest = { isDropdownExpanded.value = false }) {
            items.filter { !selectedItems.contains(it) }.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelectionChanged(item)
                    isDropdownExpanded.value = false
                }) {
                    Text(item)
                }
            }
        }
    }
}