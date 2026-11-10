package com.rebuket.mvp.ui.eco
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.local.entity.EcoBouquetEntity
import com.rebuket.mvp.data.repository.EcoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EcoViewModel @Inject constructor(private val repo: EcoRepository) : ViewModel() {
    val bouquets: StateFlow<List<EcoBouquetEntity>> = repo.getBouquets().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun addBouquet(titleRu: String, titleTj: String, titleEn: String, price: Double, contact: String) {
        viewModelScope.launch { repo.addBouquet(EcoBouquetEntity(titleRu=titleRu, titleTj=titleTj, titleEn=titleEn, price=price, contact=contact)) }
    }
}