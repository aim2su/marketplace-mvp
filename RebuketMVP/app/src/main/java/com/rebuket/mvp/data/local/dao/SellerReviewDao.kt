package com.rebuket.mvp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rebuket.mvp.data.local.entity.SellerReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SellerReviewDao {
    @Insert
    suspend fun insert(review: SellerReviewEntity)
    @Query("SELECT * FROM seller_reviews WHERE sellerId = :sellerId ORDER BY timestamp DESC")
    fun getBySeller(sellerId: Long): Flow<List<SellerReviewEntity>>
}