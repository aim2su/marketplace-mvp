package com.rebuket.mvp.ui.shops

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocalAppLocale
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.ui.catalog.CatalogViewModel
import com.rebuket.mvp.ui.components.ShopLogo
import com.rebuket.mvp.ui.components.StarRatingDisplay

@Composable
fun ShopsScreen(
    viewModel: CatalogViewModel = hiltViewModel(),
    onShopClick: (Long) -> Unit
) {
    val C = LocalExtendedPalette.current
    val locale = LocalAppLocale.current
    val shops by viewModel.shops.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()

    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("all") }

    val filtered = remember(shops, products, query, filter) {
        var list = shops
        if (query.isNotBlank()) {
            val q = query.lowercase()
            list = list.filter {
                it.shop.getLocalizedName(locale).lowercase().contains(q) ||
                it.shop.address.lowercase().contains(q)
            }
        }
        when (filter) {
            "has-items" -> list = list.filter { shop -> products.any { it.shopId == shop.shop.id } }
            "top-rated" -> list = list.sortedByDescending { it.avgRating ?: 0.0 }
        }
        list
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text(stringResource(R.string.search_shops_hint), fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = C.muted) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "all" to R.string.filter_all,
                "has-items" to R.string.filter_has_items,
                "top-rated" to R.string.filter_top_rated
            ).forEach { (k, res) ->
                val active = filter == k
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (active) C.primary else C.surface)
                        .clickable { filter = k }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        stringResource(res),
                        color = if (active) Color.White else C.text,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Text(
            stringResource(R.string.shops_count, filtered.size),
            color = C.muted,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            items(filtered, key = { it.shop.id }) { item ->
                Card(
                    onClick = { onShopClick(item.shop.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = C.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ShopLogo(logoRes = item.shop.logoRes, size = 56.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.shop.getLocalizedName(locale), color = C.text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(item.shop.address, color = C.muted, fontSize = 12.sp)
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                                if (item.avgRating != null && item.reviewCount > 0) {
                                    StarRatingDisplay(item.avgRating ?: 0.0, starSize = 12.dp)
                                    Text(
                                        " ${String.format("%.1f", item.avgRating)} (${item.reviewCount})",
                                        color = C.muted,
                                        fontSize = 11.sp
                                    )
                                } else {
                                    Text(stringResource(R.string.no_ratings), color = C.muted, fontSize = 11.sp)
                                }
                            }
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = C.muted, modifier = Modifier.size(18.dp))
                    }
                }
            }
            if (filtered.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.no_shops_found), color = C.muted, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}