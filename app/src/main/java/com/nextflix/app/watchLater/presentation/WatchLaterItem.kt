package com.nextflix.app.watchLater.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nextflix.app.watchLater.data.model.WatchLaterSeries

@Composable
fun WatchLaterItem(){
    var isSelected by remember { mutableStateOf(false) }

    // Watch later/Check icon
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(34.dp)
            //.align(Alignment.TopEnd)
            .clip(RoundedCornerShape(8.dp))
            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(8.dp))
            .clickable { isSelected = !isSelected }
            .background(if (isSelected) Color.White else Color.Black.copy(alpha = 0.5f))
        ,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSelected) Icons.Filled.Check else Icons.Outlined.WatchLater,
            contentDescription = "Add to watch list",
            tint = if (isSelected) Color.Black else Color.White
        )
    }
}