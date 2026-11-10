package com.rebuket.mvp.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.local.entity.ProductEntity
import com.rebuket.mvp.data.local.model.ShopWithRating
import com.rebuket.mvp.data.repository.CartRepository
import com.rebuket.mvp.data.repository.CatalogRepository
import com.rebuket.mvp.data.repository.SellerReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository,
    private val reviewRepository: SellerReviewRepository
) : ViewModel() {

    private val productId: Long = checkNotNull(savedStateHandle["id"])

    val product: StateFlow<ProductEntity?> = catalogRepository.getProductById(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val seller: StateFlow<ShopWithRating?> = product.flatMapLatest { p ->
        if (p == null) flowOf(null) else catalogRepository.getShopById(p.shopId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val quantityInCart: StateFlow<Int> = cartRepository.getCartItems()
        .map { items -> items.find { it.product.id == productId }?.quantity ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addToCart() { viewModelScope.launch { cartRepository.addToCart(productId) } }
    fun increment(current: Int) { viewModelScope.launch { cartRepository.updateQuantity(productId, current + 1) } }
    fun decrement(current: Int) { viewModelScope.launch { cartRepository.updateQuantity(productId, current - 1) } }
    fun toggleFavorite(item: ProductEntity) {
        viewModelScope.launch { catalogRepository.updateFavorite(item.id, !item.isFavorite) }
    }
    fun submitReview(rating: Int, comment: String) {
        val sellerId = seller.value?.shop?.id ?: return
        viewModelScope.launch { reviewRepository.addReview(sellerId, rating, comment) }
    }
}