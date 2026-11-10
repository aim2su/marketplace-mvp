package com.rebuket.mvp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rebuket.mvp.R

fun getProductImages(id: Long): List<Int> {
    return when (id) {
        1L -> listOf(R.drawable.flow1, R.drawable.flow2, R.drawable.flow3)
        2L -> listOf(R.drawable.flow4, R.drawable.flow5, R.drawable.flow6)
        3L -> listOf(R.drawable.flow7, R.drawable.flow8, R.drawable.flow9)
        4L -> listOf(R.drawable.roses, R.drawable.flow1, R.drawable.flow2)
        5L -> listOf(R.drawable.flow3, R.drawable.flow4, R.drawable.flow5)
        6L -> listOf(R.drawable.flow6, R.drawable.flow7, R.drawable.flow8)
        7L -> listOf(R.drawable.flow9, R.drawable.roses, R.drawable.flow1)
        8L -> listOf(R.drawable.flow2, R.drawable.flow3, R.drawable.flow4)
        9L -> listOf(R.drawable.flow5, R.drawable.flow6, R.drawable.flow7)
        else -> listOf(R.drawable.flow8, R.drawable.flow9, R.drawable.roses)
    }
}

@Composable
fun ImageSlider(images: List<Int>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { images.size })
    Box(modifier = modifier) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (images.size > 1) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(images.size) { index ->
                    val color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
        }
    }
}