package com.rebuket.mvp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.rebuket.mvp.data.local.entity.ShopEntity
import com.rebuket.mvp.data.local.model.ShopWithRating
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM shops")
    fun getAll(): Flow<List<ShopEntity>>

    @Query("SELECT * FROM shops WHERE id = :id")
    fun getById(id: Long): Flow<ShopEntity?>

    @Query("""
        SELECT s.*,
            (SELECT AVG(rating) FROM seller_reviews WHERE sellerId = s.id) AS avgRating,
            (SELECT COUNT(*) FROM seller_reviews WHERE sellerId = s.id) AS reviewCount
        FROM shops s
    """)
    fun getAllWithRating(): Flow<List<ShopWithRating>>

    @Query("""
        SELECT s.*,
            (SELECT AVG(rating) FROM seller_reviews WHERE sellerId = s.id) AS avgRating,
            (SELECT COUNT(*) FROM seller_reviews WHERE sellerId = s.id) AS reviewCount
        FROM shops s WHERE s.id = :id
    """)
    fun getWithRatingById(id: Long): Flow<ShopWithRating?>
}