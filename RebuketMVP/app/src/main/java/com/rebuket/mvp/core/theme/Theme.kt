package com.rebuket.mvp.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private fun buildTypography(scale: Float): Typography {
    fun ts(size: Int, weight: FontWeight = FontWeight.Normal, lineHeight: Int = size + 6) = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = weight,
        fontSize = (size * scale).sp,
        lineHeight = (lineHeight * scale).sp
    )
    return Typography(
        displayLarge = ts(34, FontWeight.Bold, 40),
        headlineMedium = ts(24, FontWeight.SemiBold, 30),
        titleLarge = ts(20, FontWeight.SemiBold, 26),
        titleMedium = ts(16, FontWeight.Medium, 22),
        bodyLarge = ts(16, FontWeight.Normal, 22),
        bodyMedium = ts(14, FontWeight.Normal, 20),
        labelLarge = ts(14, FontWeight.Medium, 18),
        labelSmall = ts(12, FontWeight.Normal, 16)
    )
}

@Composable
fun RebuketTheme(
    themeMode: String = "SYSTEM",
    fontSizeKey: String = "m",
    paletteKey: String = "classic",
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        "LIGHT" -> false
        "DARK" -> true
        else -> isSystemInDarkTheme()
    }
    val palette = getPalette(paletteKey, isDark)
    val fontScale = getFontScale(fontSizeKey)

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = palette.primary,
            onPrimary = palette.white,
            primaryContainer = palette.primaryLight,
            onPrimaryContainer = palette.text,
            background = palette.bg,
            onBackground = palette.text,
            surface = palette.surface,
            onSurface = palette.text,
            surfaceVariant = palette.bg,
            onSurfaceVariant = palette.muted,
            error = palette.danger,
            outline = palette.border
        )
    } else {
        lightColorScheme(
            primary = palette.primary,
            onPrimary = palette.white,
            primaryContainer = palette.primaryLight,
            onPrimaryContainer = palette.text,
            background = palette.bg,
            onBackground = palette.text,
            surface = palette.surface,
            onSurface = palette.text,
            surfaceVariant = palette.bg,
            onSurfaceVariant = palette.muted,
            error = palette.danger,
            outline = palette.border
        )
    }

    CompositionLocalProvider(LocalExtendedPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = buildTypography(fontScale)
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = palette.bg) {
                content()
            }
        }
    }
}