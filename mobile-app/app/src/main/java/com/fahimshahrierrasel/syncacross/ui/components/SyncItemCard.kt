package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahimshahrierrasel.syncacross.R
import com.fahimshahrierrasel.syncacross.data.models.ItemType
import com.fahimshahrierrasel.syncacross.data.models.SyncItem
import com.fahimshahrierrasel.syncacross.ui.theme.ErrorColor
import com.fahimshahrierrasel.syncacross.ui.theme.PrimaryColor
import com.fahimshahrierrasel.syncacross.utils.toFormatted
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun SyncItemCard(syncItem: SyncItem, onUpdateClicked: () -> Unit, onDeleteClicked: () -> Unit) {
    fun getIcon(): ImageVector {
        return when (syncItem.type) {
            ItemType.BOOKMARK.value -> {
                Icons.Rounded.BookmarkBorder
            }
            ItemType.MEDIA.value -> {
                Icons.Rounded.Movie
            }
            ItemType.NOTE.value -> {
                Icons.Rounded.Note
            }
            else -> {
                Icons.Rounded.HelpOutline
            }
        }
    }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp), elevation = 4.dp
    ) {
        Surface(Modifier.padding(10.dp)) {
            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            syncItem.title ?: "Untitled",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                getIcon(),
                                contentDescription = stringResource(R.string.content_type),
                                tint = PrimaryColor,
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                " | ${syncItem.createdAt.toFormatted()} | ${syncItem.origin}",
                                fontSize = 12.sp
                            )
                        }
                    }
                    Row() {
                        Icon(
                            Icons.Rounded.Edit,
                            tint = PrimaryColor,
                            contentDescription = stringResource(R.string.edit),
                            modifier = Modifier.clickable(onClick = onUpdateClicked)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Rounded.DeleteOutline,
                            tint = ErrorColor,
                            contentDescription = stringResource(R.string.delete),
                            modifier = Modifier.clickable(onClick = onDeleteClicked)
                        )
                    }
                }
                Text(syncItem.value, modifier = Modifier.padding(vertical = 4.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisSpacing = 4.dp
                ) {
                    repeat(syncItem.tags?.size ?: 0) {
                        if (syncItem.tags?.get(it) != null) {
                            Tag(syncItem.tags!![it])
                        }
                    }
                }
            }
        }
    }
}