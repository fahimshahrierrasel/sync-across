package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahimshahrierrasel.syncacross.ui.theme.PrimaryColor

@Composable
fun Tag(
    label: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    isSelected: Boolean = false,
    textPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
    onDelete: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier,
        color = if (isSelected) PrimaryColor else Color.Transparent,
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(2.dp, PrimaryColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(textPadding)
        ) {
            Text(
                label,
                fontSize = fontSize
            )
            if (onDelete != null) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(onClick = onDelete)
                )
            }
        }
    }
}

@Composable
fun SmallButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(2.dp, PrimaryColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}