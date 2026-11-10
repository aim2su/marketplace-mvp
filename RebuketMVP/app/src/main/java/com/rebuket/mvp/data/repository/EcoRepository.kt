package com.rebuket.mvp.data.repository
import com.rebuket.mvp.data.local.dao.EcoDao
import com.rebuket.mvp.data.local.entity.EcoBouquetEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface EcoRepository {
    fun getBouquets(): Flow<List<EcoBouquetEntity>>
    suspend fun addBouquet(bouquet: EcoBouquetEntity)
}
class EcoRepositoryImpl @Inject constructor(private val dao: EcoDao) : EcoRepository {
    override fun getBouquets(): Flow<List<EcoBouquetEntity>> = dao.getAll()
    override suspend fun addBouquet(bouquet: EcoBouquetEntity) = dao.insert(bouquet)
}