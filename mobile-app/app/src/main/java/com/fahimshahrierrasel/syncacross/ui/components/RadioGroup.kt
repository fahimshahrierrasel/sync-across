package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fahimshahrierrasel.syncacross.ui.localCapitalize
import com.fahimshahrierrasel.syncacross.ui.theme.PrimaryColor


data class RadioGroupItem(val title: String, val value: String) {}

@Composable
fun RadioGroup(
    items: List<RadioGroupItem>,
    selectedItem: String,
    onItemClick: (key: String) -> Unit
) {
    Row(Modifier.selectableGroup()) {
        items.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                RadioButton(
                    selected = item.value == selectedItem,
                    onClick = { onItemClick(item.value) },
                    colors = RadioButtonDefaults.colors(selectedColor = PrimaryColor)
                )
                Text(item.title.localCapitalize())
            }
        }
    }
}