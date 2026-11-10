package com.rebuket.mvp.data.local.dao
import androidx.room.Dao
import androidx.room.Query
import com.rebuket.mvp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<ProductEntity>>
    @Query("SELECT * FROM products WHERE id = :id")
    fun getById(id: Long): Flow<ProductEntity?>
    @Query("SELECT * FROM products WHERE shopId = :shopId")
    fun getByShopId(shopId: Long): Flow<List<ProductEntity>>
    @Query("SELECT * FROM products WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<ProductEntity>>
    @Query("UPDATE products SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFav: Boolean)
}