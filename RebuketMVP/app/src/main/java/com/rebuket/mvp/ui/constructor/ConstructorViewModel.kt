package com.rebuket.mvp.ui.constructor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FlowerType(val key: String, val nameRes: Int, val price: Int)
data class PackagingOption(val key: String, val nameRes: Int, val price: Int)
data class RibbonOption(val key: String, val nameRes: Int, val price: Int)

val CONSTRUCTOR_TYPES = listOf("bouquet", "cake", "sweet")

val FLOWER_TYPES = listOf(
    FlowerType("roses", com.rebuket.mvp.R.string.flower_roses, 25),
    FlowerType("peonies", com.rebuket.mvp.R.string.flower_peonies, 35),
    FlowerType("tulips", com.rebuket.mvp.R.string.flower_tulips, 15),
    FlowerType("hydrangeas", com.rebuket.mvp.R.string.flower_hydrangeas, 60)
)

val PACKAGING_OPTIONS = listOf(
    PackagingOption("craft", com.rebuket.mvp.R.string.packaging_craft, 20),
    PackagingOption("box", com.rebuket.mvp.R.string.packaging_box, 80),
    PackagingOption("film", com.rebuket.mvp.R.string.packaging_film, 15)
)

val RIBBON_OPTIONS = listOf(
    RibbonOption("atlas", com.rebuket.mvp.R.string.ribbon_atlas, 10),
    RibbonOption("jute", com.rebuket.mvp.R.string.ribbon_jute, 8),
    RibbonOption("none", com.rebuket.mvp.R.string.ribbon_none, 0)
)

@HiltViewModel
class ConstructorViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {
    val type = MutableStateFlow("bouquet")
    val flowers = MutableStateFlow<Map<String, Int>>(mapOf("roses" to 5, "peonies" to 0, "tulips" to 0, "hydrangeas" to 0))
    val packaging = MutableStateFlow("craft")
    val ribbon = MutableStateFlow("atlas")
    val card = MutableStateFlow("")

    val totalFlowers: StateFlow<Int> = flowers
        .map { it.values.sum() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 5)

    val flowersTotal: StateFlow<Int> = flowers
        .map { map ->
            map.entries.sumOf { (key, count) ->
                (FLOWER_TYPES.find { it.key == key }?.price ?: 0) * count
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalPrice: StateFlow<Int> = combine(flowers, packaging, ribbon) { fl, pack, rib ->
        val fTotal = fl.entries.sumOf { (key, count) ->
            (FLOWER_TYPES.find { it.key == key }?.price ?: 0) * count
        }
        val pPrice = PACKAGING_OPTIONS.find { it.key == pack }?.price ?: 0
        val rPrice = RIBBON_OPTIONS.find { it.key == rib }?.price ?: 0
        fTotal + pPrice + rPrice
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setType(t: String) { type.value = t }
    fun changeFlower(key: String, delta: Int) {
        val current = flowers.value[key] ?: 0
        val next = (current + delta).coerceAtLeast(0)
        flowers.value = flowers.value.toMutableMap().also { it[key] = next }
    }
    fun setPackaging(p: String) { packaging.value = p }
    fun setRibbon(r: String) { ribbon.value = r }
    fun setCard(c: String) { if (c.length <= 300) card.value = c }

    fun submitOrder(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (totalFlowers.value > 0) {
                orderRepository.createOrder(totalPrice.value.toDouble(), totalFlowers.value, "")
                flowers.value = mapOf("roses" to 0, "peonies" to 0, "tulips" to 0, "hydrangeas" to 0)
                card.value = ""
                onSuccess()
            }
        }
    }
}