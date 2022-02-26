package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahimshahrierrasel.syncacross.R
import com.fahimshahrierrasel.syncacross.ui.theme.ErrorColor
import com.fahimshahrierrasel.syncacross.ui.theme.PrimaryColor
import com.fahimshahrierrasel.syncacross.utils.toFormatted
import com.google.accompanist.flowlayout.FlowRow
import java.util.*

@Preview
@Composable
fun SyncItemCard() {
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text("Title of the Item", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Movie,
                                contentDescription = stringResource(R.string.content_type)
                            )
                            Text(" | ${Date().toFormatted()} | Device")
                        }
                    }
                    Row() {
                        Icon(
                            Icons.Rounded.Edit,
                            tint = PrimaryColor,
                            contentDescription = stringResource(R.string.edit),
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clickable { }
                        )
                        Icon(
                            Icons.Rounded.DeleteOutline,
                            tint = ErrorColor,
                            contentDescription = stringResource(R.string.delete),
                            modifier = Modifier.clickable { }
                        )

                    }
                }
                Text(stringResource(R.string.lorem), modifier = Modifier.padding(vertical = 4.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisSpacing = 4.dp
                ) {
                    Tag(label = "Tag 1")
                    Tag(label = "Tag 2")
                }
            }
        }
    }
}