package com.rebuket.mvp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seller_reviews")
data class SellerReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sellerId: Long,
    val rating: Int,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)