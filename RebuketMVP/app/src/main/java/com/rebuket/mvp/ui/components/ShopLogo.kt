package com.rebuket.mvp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rebuket.mvp.R

@Composable
fun ShopLogo(logoRes: String, size: Dp = 64.dp) {
    val resId = when (logoRes) {
        "logo1" -> R.drawable.logo1
        "logo2" -> R.drawable.logo2
        "logo3" -> R.drawable.logo3
        "logo4" -> R.drawable.logo4
        else -> R.drawable.ic_custom_flower
    }
    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        modifier = Modifier.size(size).clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop
    )
}