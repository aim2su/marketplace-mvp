package com.rebuket.mvp.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.local.entity.ProductEntity
import com.rebuket.mvp.data.local.model.ShopWithRating
import com.rebuket.mvp.data.repository.CartRepository
import com.rebuket.mvp.data.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortOrder { DEFAULT, PRICE_ASC, PRICE_DESC }

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val allProducts = repository.getProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shops: StateFlow<List<ShopWithRating>> = repository.getShops()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _sortOrder = MutableStateFlow(SortOrder.DEFAULT)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    private val _minPrice = MutableStateFlow("")
    val minPrice: StateFlow<String> = _minPrice

    private val _maxPrice = MutableStateFlow("")
    val maxPrice: StateFlow<String> = _maxPrice

    private val _selectedShopId = MutableStateFlow<Long?>(null)
    val selectedShopId: StateFlow<Long?> = _selectedShopId

    val isFilterActive: StateFlow<Boolean> = combine(_sortOrder, _minPrice, _maxPrice, _selectedShopId) { sort, min, max, shop ->
        sort != SortOrder.DEFAULT || min.isNotEmpty() || max.isNotEmpty() || shop != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val cartQuantities: StateFlow<Map<Long, Int>> = cartRepository.getCartItems()
        .map { items -> items.associate { it.product.id to it.quantity } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val products: StateFlow<List<ProductEntity>> = combine(
        allProducts, _searchQuery, _sortOrder, _minPrice, _maxPrice, _selectedShopId
    ) { values ->
        val products = values[0] as List<ProductEntity>
        val query = values[1] as String
        val sort = values[2] as SortOrder
        val minStr = values[3] as String
        val maxStr = values[4] as String
        val shopId = values[5] as Long?

        val minVal = minStr.toDoubleOrNull() ?: 0.0
        val maxVal = maxStr.toDoubleOrNull() ?: Double.MAX_VALUE

        products.filter {
            it.price in minVal..maxVal &&
                (shopId == null || it.shopId == shopId) &&
                (it.nameRu.contains(query, true) || it.nameTj.contains(query, true) || it.nameEn.contains(query, true))
        }.let { list ->
            when (sort) {
                SortOrder.PRICE_ASC -> list.sortedBy { it.price }
                SortOrder.PRICE_DESC -> list.sortedByDescending { it.price }
                else -> list
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun clearSearch() { _searchQuery.value = "" }
    fun setSortOrder(order: SortOrder) { _sortOrder.value = order }
    fun setMinPrice(price: String) { _minPrice.value = price }
    fun setMaxPrice(price: String) { _maxPrice.value = price }
    fun setShopFilter(shopId: Long?) { _selectedShopId.value = shopId }

    fun resetFilters() {
        _sortOrder.value = SortOrder.DEFAULT
        _minPrice.value = ""
        _maxPrice.value = ""
        _selectedShopId.value = null
    }

    fun resetAll() {
        resetFilters()
        _searchQuery.value = ""
    }

    fun addToCart(productId: Long) { viewModelScope.launch { cartRepository.addToCart(productId) } }
    fun increment(productId: Long, current: Int) { viewModelScope.launch { cartRepository.updateQuantity(productId, current + 1) } }
    fun decrement(productId: Long, current: Int) { viewModelScope.launch { cartRepository.updateQuantity(productId, current - 1) } }
    fun toggleFavorite(product: ProductEntity) {
        viewModelScope.launch { repository.updateFavorite(product.id, !product.isFavorite) }
    }
}