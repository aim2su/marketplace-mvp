package com.rebuket.mvp.data.local.model

import androidx.room.Embedded
import com.rebuket.mvp.data.local.entity.ProductEntity

data class CartItemWithProduct(
    @Embedded val product: ProductEntity,
    val quantity: Int
) {
    val totalPrice: Double get() = product.price * quantity
}