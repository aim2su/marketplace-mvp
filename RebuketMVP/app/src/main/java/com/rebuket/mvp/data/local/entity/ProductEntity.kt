package com.rebuket.mvp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Long,
    val nameRu: String,
    val nameTj: String,
    val nameEn: String,
    val price: Double,
    val shopId: Long,
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val description: String = "",
    val oldPrice: Double? = null,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val deliveryHours: Int? = null,
    val inStock: Boolean = true,
    val category: String = "Все"
) {
    fun getLocalizedName(locale: String): String = when (locale) {
        "tj" -> nameTj
        "en" -> nameEn
        else -> nameRu
    }
    fun getDiscountPercent(): Int {
        val old = oldPrice ?: return 0
        if (old <= 0.0 || old <= price) return 0
        return ((old - price) / old * 100).toInt()
    }
}