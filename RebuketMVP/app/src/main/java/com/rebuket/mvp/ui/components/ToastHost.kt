package com.rebuket.mvp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.core.toast.LocalToast

@Composable
fun ToastHost() {
    val C = LocalExtendedPalette.current
    val toast = LocalToast.current
    val message by toast.message.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize().padding(bottom = 140.dp)) {
        AnimatedVisibility(
            visible = message != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .widthIn(max = 360.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(C.text)
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Text(
                    text = message ?: "",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}