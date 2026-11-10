package com.rebuket.mvp.data.repository

import com.rebuket.mvp.data.local.dao.ProductDao
import com.rebuket.mvp.data.local.dao.ShopDao
import com.rebuket.mvp.data.local.entity.ProductEntity
import com.rebuket.mvp.data.local.model.ShopWithRating
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CatalogRepository {
    fun getProducts(): Flow<List<ProductEntity>>
    fun getProductById(id: Long): Flow<ProductEntity?>
    fun getShops(): Flow<List<ShopWithRating>>
    fun getShopById(id: Long): Flow<ShopWithRating?>
    fun getProductsByShopId(shopId: Long): Flow<List<ProductEntity>>
    fun getFavorites(): Flow<List<ProductEntity>>
    suspend fun updateFavorite(id: Long, isFav: Boolean)
}

class CatalogRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val shopDao: ShopDao
) : CatalogRepository {
    override fun getProducts(): Flow<List<ProductEntity>> = productDao.getAll()
    override fun getProductById(id: Long): Flow<ProductEntity?> = productDao.getById(id)
    override fun getShops(): Flow<List<ShopWithRating>> = shopDao.getAllWithRating()
    override fun getShopById(id: Long): Flow<ShopWithRating?> = shopDao.getWithRatingById(id)
    override fun getProductsByShopId(shopId: Long): Flow<List<ProductEntity>> = productDao.getByShopId(shopId)
    override fun getFavorites(): Flow<List<ProductEntity>> = productDao.getFavorites()
    override suspend fun updateFavorite(id: Long, isFav: Boolean) = productDao.updateFavorite(id, isFav)
}