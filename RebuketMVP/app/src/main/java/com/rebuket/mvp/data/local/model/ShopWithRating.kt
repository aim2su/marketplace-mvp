package com.rebuket.mvp.data.local.model

import androidx.room.Embedded
import com.rebuket.mvp.data.local.entity.ShopEntity

data class ShopWithRating(
    @Embedded val shop: ShopEntity,
    val avgRating: Double?,
    val reviewCount: Int
)