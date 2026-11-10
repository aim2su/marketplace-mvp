package com.rebuket.mvp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shops")
data class ShopEntity(
    @PrimaryKey val id: Long,
    val nameRu: String,
    val nameTj: String,
    val nameEn: String,
    val address: String,
    val logoRes: String = "",
    val descriptionRu: String = "",
    val descriptionTj: String = "",
    val descriptionEn: String = "",
    val isOpen: Boolean = true,
    val verified: Boolean = false,
    val deliveryTime: String = "2 часа",
    val minOrder: Int = 300,
    val deliveryFee: Int = 30,
    val hours: String = "09:00 – 21:00"
) {
    fun getLocalizedName(locale: String): String = when (locale) {
        "tj" -> nameTj
        "en" -> nameEn
        else -> nameRu
    }
    fun getLocalizedDescription(locale: String): String = when (locale) {
        "tj" -> descriptionTj
        "en" -> descriptionEn
        else -> descriptionRu
    }
}