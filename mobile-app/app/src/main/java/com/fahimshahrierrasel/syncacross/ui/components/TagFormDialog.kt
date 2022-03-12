package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fahimshahrierrasel.syncacross.ui.theme.NeutralColor
import com.fahimshahrierrasel.syncacross.viewmodels.HomeUIAction
import com.fahimshahrierrasel.syncacross.viewmodels.HomeViewModel

@Composable
fun TagFormDialog(
    viewModel: HomeViewModel,
    onDismiss: () -> Unit,
    onSave: (title: String) -> Unit
) {
    val title = remember { mutableStateOf("") }

    fun onFormSubmit() {
        if (title.value.length < 3) {
            viewModel.onAction(HomeUIAction.ShowMessage("Title is too short"))
            return
        }
        onSave(title.value)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(8.dp)) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text("New Tag")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    value = title.value,
                    onValueChange = { title.value = it },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(backgroundColor = NeutralColor),
                        modifier = Modifier.width(85.dp)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(onClick = { onFormSubmit() }, modifier = Modifier.width(85.dp)) {
                        Text("Save")
                    }
                }
            }
        }
    }
}