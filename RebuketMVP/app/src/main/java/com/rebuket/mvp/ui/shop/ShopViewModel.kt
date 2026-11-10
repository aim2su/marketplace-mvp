package com.rebuket.mvp.ui.shop

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.local.entity.ProductEntity
import com.rebuket.mvp.data.local.entity.SellerReviewEntity
import com.rebuket.mvp.data.local.model.ShopWithRating
import com.rebuket.mvp.data.repository.CartRepository
import com.rebuket.mvp.data.repository.CatalogRepository
import com.rebuket.mvp.data.repository.SellerReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catalogRepo: CatalogRepository,
    private val cartRepository: CartRepository,
    private val reviewRepository: SellerReviewRepository
) : ViewModel() {
    private val shopId: Long = checkNotNull(savedStateHandle["id"])

    val shop: StateFlow<ShopWithRating?> = catalogRepo.getShopById(shopId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val products: StateFlow<List<ProductEntity>> = catalogRepo.getProductsByShopId(shopId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviews: StateFlow<List<SellerReviewEntity>> = reviewRepository.getReviews(shopId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartQuantities: StateFlow<Map<Long, Int>> = cartRepository.getCartItems()
        .map { items -> items.associate { it.product.id to it.quantity } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun addToCart(productId: Long) { viewModelScope.launch { cartRepository.addToCart(productId) } }
    fun increment(productId: Long, current: Int) { viewModelScope.launch { cartRepository.updateQuantity(productId, current + 1) } }
    fun decrement(productId: Long, current: Int) { viewModelScope.launch { cartRepository.updateQuantity(productId, current - 1) } }
    fun toggleFavorite(product: ProductEntity) { viewModelScope.launch { catalogRepo.updateFavorite(product.id, !product.isFavorite) } }
    fun submitReview(rating: Int, comment: String) {
        viewModelScope.launch { reviewRepository.addReview(shopId, rating, comment) }
    }
}