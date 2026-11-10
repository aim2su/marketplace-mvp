package com.rebuket.mvp.ui.shops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.local.model.ShopWithRating
import com.rebuket.mvp.data.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(repo: CatalogRepository) : ViewModel() {
    val shops: StateFlow<List<ShopWithRating>> = repo.getShops()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}