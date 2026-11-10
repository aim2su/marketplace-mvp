package com.rebuket.mvp.data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rebuket.mvp.data.local.entity.EcoBouquetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EcoDao {
    @Query("SELECT * FROM eco_bouquets ORDER BY id DESC")
    fun getAll(): Flow<List<EcoBouquetEntity>>
    @Insert
    suspend fun insert(item: EcoBouquetEntity)
}