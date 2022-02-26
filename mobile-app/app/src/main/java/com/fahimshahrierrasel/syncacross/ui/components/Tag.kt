package com.fahimshahrierrasel.syncacross.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahimshahrierrasel.syncacross.ui.theme.PrimaryColor

@Composable
fun Tag(label: String, modifier: Modifier = Modifier, fontSize: TextUnit = 12.sp, textPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(2.dp, PrimaryColor)
    ) {
        Text(
            label,
            fontSize = fontSize,
            modifier = Modifier.padding(textPadding)
        )
    }
}