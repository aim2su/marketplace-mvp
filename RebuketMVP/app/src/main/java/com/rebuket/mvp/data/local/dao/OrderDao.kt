package com.rebuket.mvp.data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rebuket.mvp.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: OrderEntity): Long
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAll(): Flow<List<OrderEntity>>
}