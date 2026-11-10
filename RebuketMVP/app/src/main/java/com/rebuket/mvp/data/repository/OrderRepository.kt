package com.rebuket.mvp.data.repository

import com.rebuket.mvp.data.local.dao.OrderDao
import com.rebuket.mvp.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OrderRepository {
    suspend fun createOrder(total: Double, count: Int, productIds: String)
    fun getOrders(): Flow<List<OrderEntity>>
}

class OrderRepositoryImpl @Inject constructor(private val dao: OrderDao) : OrderRepository {
    override suspend fun createOrder(total: Double, count: Int, productIds: String) {
        dao.insert(OrderEntity(totalPrice = total, itemCount = count, productIds = productIds))
    }
    override fun getOrders(): Flow<List<OrderEntity>> = dao.getAll()
}