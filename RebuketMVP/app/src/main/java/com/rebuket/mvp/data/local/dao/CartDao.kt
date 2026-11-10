package com.rebuket.mvp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rebuket.mvp.data.local.entity.CartEntity
import com.rebuket.mvp.data.local.model.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query(
        "SELECT p.*, c.quantity FROM cart c INNER JOIN products p ON p.id = c.productId"
    )
    fun getCartItems(): Flow<List<CartItemWithProduct>>

    @Query("SELECT * FROM cart WHERE productId = :id")
    suspend fun getById(id: Long): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartEntity)

    @Query("UPDATE cart SET quantity = :qty WHERE productId = :id")
    suspend fun updateQuantity(id: Long, qty: Int)

    @Query("DELETE FROM cart WHERE productId = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM cart")
    suspend fun clear()
}