package com.rebuket.mvp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
private val StarGold = Color(0xFFFFB300)

@Composable
fun StarRatingDisplay(rating: Double, starSize: Dp = 16.dp) {
    Row {
        for (i in 1..5) {
            val filled = i <= rating.toInt() || (i - rating) < 1.0 && rating - (i - 1) >= 0.5
            Icon(
                imageVector = if (i <= rating + 0.5) Icons.Filled.Star else Icons.Filled.Star,
                contentDescription = null,
                tint = if (i <= rating + 0.5) StarGold else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(starSize)
            )
        }
    }
}

@Composable
fun StarRatingInput(rating: Int, onRatingChange: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            IconButton(onClick = { onRatingChange(i) }) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.Star,
                    contentDescription = null,
                    tint = if (i <= rating) StarGold else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}