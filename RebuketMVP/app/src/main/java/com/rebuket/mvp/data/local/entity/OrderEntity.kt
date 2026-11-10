package com.rebuket.mvp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val totalPrice: Double,
    val itemCount: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "В обработке",
    val productIds: String = ""
)