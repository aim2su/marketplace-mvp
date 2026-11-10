package com.rebuket.mvp.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.local.entity.OrderEntity
import com.rebuket.mvp.data.local.entity.ProductEntity
import com.rebuket.mvp.data.repository.CatalogRepository
import com.rebuket.mvp.data.repository.OrderRepository
import com.rebuket.mvp.data.repository.SellerReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderWithProducts(val order: OrderEntity, val products: List<ProductEntity>)

@HiltViewModel
class OrdersViewModel @Inject constructor(
    orderRepo: OrderRepository,
    catalogRepo: CatalogRepository,
    private val reviewRepo: SellerReviewRepository
) : ViewModel() {

    private val ordersFlow = orderRepo.getOrders()
    private val productsFlow = catalogRepo.getProducts()

    val ordersWithProducts: StateFlow<List<OrderWithProducts>> = combine(ordersFlow, productsFlow) { orders, allProducts ->
        orders.map { order ->
            val ids = order.productIds.split(",").mapNotNull { it.trim().toLongOrNull() }.filter { it > 0 }
            val products = allProducts.filter { it.id in ids }
            OrderWithProducts(order, products)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun submitReview(sellerId: Long, rating: Int, comment: String) {
        viewModelScope.launch { reviewRepo.addReview(sellerId, rating, comment) }
    }
}