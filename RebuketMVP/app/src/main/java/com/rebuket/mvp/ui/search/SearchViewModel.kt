package com.rebuket.mvp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.local.entity.ProductEntity
import com.rebuket.mvp.data.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(repo: CatalogRepository) : ViewModel() {
    private val allProducts = repo.getProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _cat = MutableStateFlow("Все")
    val category: StateFlow<String> = _cat

    val results: StateFlow<List<ProductEntity>> = combine(allProducts, _query, _cat) { products, q, cat ->
        products.filter {
            (cat == "Все" || it.category == cat) &&
                (q.isBlank() ||
                    it.nameRu.contains(q, true) ||
                    it.nameTj.contains(q, true) ||
                    it.nameEn.contains(q, true))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setQuery(q: String) { _query.value = q }
    fun setCategory(c: String) { _cat.value = c }
    fun reset() { _query.value = ""; _cat.value = "Все" }
}