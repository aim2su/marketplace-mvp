package com.rebuket.mvp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.local.entity.ProductEntity
import com.rebuket.mvp.data.repository.CartRepository
import com.rebuket.mvp.data.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repo: CatalogRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    val favorites: StateFlow<List<ProductEntity>> = repo.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartQuantities: StateFlow<Map<Long, Int>> = cartRepository.getCartItems()
        .map { items -> items.associate { it.product.id to it.quantity } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun toggleFavorite(product: ProductEntity) {
        viewModelScope.launch { repo.updateFavorite(product.id, !product.isFavorite) }
    }
    fun addToCart(productId: Long) { viewModelScope.launch { cartRepository.addToCart(productId) } }
    fun increment(productId: Long, current: Int) { viewModelScope.launch { cartRepository.updateQuantity(productId, current + 1) } }
    fun decrement(productId: Long, current: Int) { viewModelScope.launch { cartRepository.updateQuantity(productId, current - 1) } }
}