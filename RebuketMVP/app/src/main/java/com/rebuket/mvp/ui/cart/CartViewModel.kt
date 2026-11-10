package com.rebuket.mvp.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.local.model.CartItemWithProduct
import com.rebuket.mvp.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartItemWithProduct>> = cartRepository.getCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalPrice: StateFlow<Double> = cartRepository.getCartItems()
        .map { items -> items.sumOf { it.totalPrice } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun increment(productId: Long, currentQty: Int) {
        viewModelScope.launch { cartRepository.updateQuantity(productId, currentQty + 1) }
    }

    fun decrement(productId: Long, currentQty: Int) {
        viewModelScope.launch { cartRepository.updateQuantity(productId, currentQty - 1) }
    }

    fun remove(productId: Long) {
        viewModelScope.launch { cartRepository.remove(productId) }
    }
}