package com.rebuket.mvp.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.repository.CartRepository
import com.rebuket.mvp.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepo: CartRepository,
    private val orderRepo: OrderRepository
) : ViewModel() {
    fun placeOrder(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val items = cartRepo.getCartItems().first()
            if (items.isNotEmpty()) {
                val productIds = items.joinToString(",") { it.product.id.toString() }
                orderRepo.createOrder(items.sumOf { it.totalPrice }, items.sumOf { it.quantity }, productIds)
                cartRepo.clear()
                onSuccess()
            }
        }
    }
}