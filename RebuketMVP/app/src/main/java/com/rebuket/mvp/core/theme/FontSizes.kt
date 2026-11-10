package com.rebuket.mvp.core.theme

data class FontSizeVariant(
    val key: String,
    val name: String,
    val scale: Float
)

val FONT_SIZES = listOf(
    FontSizeVariant("xs", "XS", 0.85f),
    FontSizeVariant("s", "S", 0.92f),
    FontSizeVariant("m", "M", 1.0f),
    FontSizeVariant("l", "L", 1.1f),
    FontSizeVariant("xl", "XL", 1.2f)
)

fun getFontScale(key: String): Float {
    return FONT_SIZES.find { it.key == key }?.scale ?: 1.0f
}