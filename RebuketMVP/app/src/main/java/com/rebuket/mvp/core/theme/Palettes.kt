package com.rebuket.mvp.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ExtendedPalette(
    val primary: Color,
    val primaryDark: Color,
    val primaryLight: Color,
    val bg: Color,
    val surface: Color,
    val text: Color,
    val muted: Color,
    val border: Color,
    val danger: Color,
    val amber: Color,
    val white: Color
)

val LocalExtendedPalette = staticCompositionLocalOf { classicLight }

val classicLight = ExtendedPalette(
    primary = Color(0xFF059669),
    primaryDark = Color(0xFF047857),
    primaryLight = Color(0xFFECFDF5),
    bg = Color(0xFFF8FAFC),
    surface = Color(0xFFFFFFFF),
    text = Color(0xFF0F172A),
    muted = Color(0xFF64748B),
    border = Color(0xFFE2E8F0),
    danger = Color(0xFFDC2626),
    amber = Color(0xFFF59E0B),
    white = Color(0xFFFFFFFF)
)

val classicDark = ExtendedPalette(
    primary = Color(0xFF34D399),
    primaryDark = Color(0xFF10B981),
    primaryLight = Color(0xFF064E3B),
    bg = Color(0xFF0A1614),
    surface = Color(0xFF17171B),
    text = Color(0xFFF1F5F9),
    muted = Color(0xFF94A3B8),
    border = Color(0xFF334155),
    danger = Color(0xFFF87171),
    amber = Color(0xFFFBBF24),
    white = Color(0xFF17171B)
)

val champagneLight = ExtendedPalette(
    primary = Color(0xFFD4A574),
    primaryDark = Color(0xFFA16207),
    primaryLight = Color(0xFFFDF7F1),
    bg = Color(0xFFFDFBF6),
    surface = Color(0xFFFFFFFF),
    text = Color(0xFF0F172A),
    muted = Color(0xFF64748B),
    border = Color(0xFFE2E8F0),
    danger = Color(0xFFDC2626),
    amber = Color(0xFFF59E0B),
    white = Color(0xFFFFFFFF)
)

val champagneDark = ExtendedPalette(
    primary = Color(0xFFE8C8A0),
    primaryDark = Color(0xFFD4A574),
    primaryLight = Color(0xFF422006),
    bg = Color(0xFF171308),
    surface = Color(0xFF17171B),
    text = Color(0xFFF1F5F9),
    muted = Color(0xFF94A3B8),
    border = Color(0xFF334155),
    danger = Color(0xFFF87171),
    amber = Color(0xFFFBBF24),
    white = Color(0xFF17171B)
)

val beigeLight = ExtendedPalette(
    primary = Color(0xFFC9A878),
    primaryDark = Color(0xFF8B6F47),
    primaryLight = Color(0xFFFAF6F0),
    bg = Color(0xFFFCFAF5),
    surface = Color(0xFFFFFFFF),
    text = Color(0xFF0F172A),
    muted = Color(0xFF64748B),
    border = Color(0xFFE2E8F0),
    danger = Color(0xFFDC2626),
    amber = Color(0xFFF59E0B),
    white = Color(0xFFFFFFFF)
)

val beigeDark = ExtendedPalette(
    primary = Color(0xFFDCC09A),
    primaryDark = Color(0xFFC9A878),
    primaryLight = Color(0xFF3A2C1A),
    bg = Color(0xFF171410),
    surface = Color(0xFF17171B),
    text = Color(0xFFF1F5F9),
    muted = Color(0xFF94A3B8),
    border = Color(0xFF334155),
    danger = Color(0xFFF87171),
    amber = Color(0xFFFBBF24),
    white = Color(0xFF17171B)
)

data class PaletteInfo(val key: String, val name: String)

val PALETTES = listOf(
    PaletteInfo("classic", "Классика"),
    PaletteInfo("champagne", "Шампань"),
    PaletteInfo("beige", "Беж")
)

fun getPalette(key: String, isDark: Boolean): ExtendedPalette = when (key) {
    "champagne" -> if (isDark) champagneDark else champagneLight
    "beige" -> if (isDark) beigeDark else beigeLight
    else -> if (isDark) classicDark else classicLight
}