package com.rebuket.mvp.data.repository

import com.rebuket.mvp.data.local.dao.CartDao
import com.rebuket.mvp.data.local.entity.CartEntity
import com.rebuket.mvp.data.local.model.CartItemWithProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CartRepository {
    fun getCartItems(): Flow<List<CartItemWithProduct>>
    suspend fun addToCart(productId: Long)
    suspend fun updateQuantity(productId: Long, quantity: Int)
    suspend fun remove(productId: Long)
    suspend fun clear()
}

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItemWithProduct>> = cartDao.getCartItems()

    override suspend fun addToCart(productId: Long) {
        val existing = cartDao.getById(productId)
        if (existing != null) {
            cartDao.updateQuantity(productId, existing.quantity + 1)
        } else {
            cartDao.insert(CartEntity(productId = productId, quantity = 1))
        }
    }

    override suspend fun updateQuantity(productId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.delete(productId)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    override suspend fun remove(productId: Long) = cartDao.delete(productId)

    override suspend fun clear() = cartDao.clear()
}