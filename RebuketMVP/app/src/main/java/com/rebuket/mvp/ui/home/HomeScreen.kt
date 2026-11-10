package com.rebuket.mvp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocalAppLocale
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.data.local.model.ShopWithRating
import com.rebuket.mvp.ui.components.ProductCard
import com.rebuket.mvp.ui.components.ShopLogo

private data class Occasion(val imageRes: Int, val titleRes: Int)

private val OCCASIONS = listOf(
    Occasion(R.drawable.occasion_bday, R.string.occasion_birthday),
    Occasion(R.drawable.occasion_date, R.string.occasion_date),
    Occasion(R.drawable.occasion_mom, R.string.occasion_mom),
    Occasion(R.drawable.occasion_wedding, R.string.occasion_wedding),
    Occasion(R.drawable.occasion_sorry, R.string.occasion_sorry),
    Occasion(R.drawable.occasion_noreason, R.string.occasion_no_reason)
)

@Composable
fun HomeScreen(
    onProductClick: (Long) -> Unit,
    onShopClick: (Long) -> Unit,
    onCatalogClick: () -> Unit,
    onShopsClick: () -> Unit,
    onConstructorClick: () -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val C = LocalExtendedPalette.current
    val products by viewModel.products.collectAsStateWithLifecycle()
    val shops by viewModel.shops.collectAsStateWithLifecycle()
    val cartQuantities by viewModel.cartQuantities.collectAsStateWithLifecycle()
    val locale = LocalAppLocale.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.home_greeting), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = C.text)
                Text(stringResource(R.string.home_subtitle), fontSize = 13.sp, color = C.muted, modifier = Modifier.padding(top = 2.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 14.dp)
                        .clip(RoundedCornerShape(14.dp)).background(C.surface)
                        .clickable { onSearchClick() }.padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Outlined.Search, contentDescription = null, tint = C.muted, modifier = Modifier.size(18.dp))
                        Text(stringResource(R.string.home_search), color = C.muted, fontSize = 14.sp)
                    }
                }
            }
        }
        item(span = { GridItemSpan(2) }) {
            HomeSectionHeader(stringResource(R.string.home_shops_title), stringResource(R.string.home_all), onShopsClick)
        }
        item(span = { GridItemSpan(2) }) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(shops, key = { it.shop.id }) { item ->
                    HomeShopCard(item, locale) { onShopClick(item.shop.id) }
                }
            }
        }
        item(span = { GridItemSpan(2) }) {
            HomeSectionHeader(stringResource(R.string.home_occasions_title))
        }
        item(span = { GridItemSpan(2) }) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(OCCASIONS) { o ->
                    Box(
                        modifier = Modifier.width(140.dp).height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onCatalogClick() }
                    ) {
                        Image(
                            painter = painterResource(id = o.imageRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, C.primaryDark.copy(alpha = 0.85f)))))
                        Text(
                            stringResource(o.titleRes),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.align(Alignment.BottomStart).padding(14.dp)
                        )
                    }
                }
            }
        }
        item(span = { GridItemSpan(2) }) {
            HomeSectionHeader(stringResource(R.string.home_popular_title), stringResource(R.string.home_all), onCatalogClick)
        }
        items(products, key = { it.id }) { product ->
            ProductCard(
                product = product,
                quantityInCart = cartQuantities[product.id] ?: 0,
                locale = locale,
                onProductClick = { onProductClick(product.id) },
                onAddToCart = { viewModel.addToCart(product.id) },
                onIncrement = { viewModel.increment(product.id, cartQuantities[product.id] ?: 0) },
                onDecrement = { viewModel.decrement(product.id, cartQuantities[product.id] ?: 0) },
                onFavoriteToggle = { viewModel.toggleFavorite(product) }
            )
        }
        item(span = { GridItemSpan(2) }) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(C.primary, C.primaryDark)))
                    .clickable { onConstructorClick() }.padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Icon(Icons.Filled.Palette, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.home_constructor_title), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(stringResource(R.string.home_constructor_subtitle), color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun HomeSectionHeader(title: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    val C = LocalExtendedPalette.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = C.text)
        if (actionLabel != null && onAction != null) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onAction() }) {
                Text(actionLabel, color = C.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = C.primary, modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun HomeShopCard(item: ShopWithRating, locale: String, onClick: () -> Unit) {
    val C = LocalExtendedPalette.current
    Column(modifier = Modifier.width(260.dp).clip(RoundedCornerShape(20.dp)).background(C.surface).clickable { onClick() }) {
        Box(modifier = Modifier.fillMaxWidth().height(130.dp)) {
            Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(C.primary, C.primaryDark))))
            Row(modifier = Modifier.align(Alignment.BottomStart).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                ShopLogo(logoRes = item.shop.logoRes, size = 56.dp)
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(item.shop.getLocalizedName(locale), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (item.shop.isOpen) Color(0xFF22C55E) else Color(0xFF94A3B8)))
                        Text(stringResource(if (item.shop.isOpen) R.string.shop_open else R.string.shop_closed), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Column(modifier = Modifier.padding(12.dp)) {
            Text(item.shop.address, color = C.muted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = C.amber, modifier = Modifier.size(12.dp))
                    Text(String.format("%.1f", item.avgRating ?: 0.0), color = C.text, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("· ${item.reviewCount}", color = C.muted, fontSize = 11.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 6.dp)) {
                Icon(Icons.Filled.Schedule, contentDescription = null, tint = C.muted, modifier = Modifier.size(10.dp))
                Text("Доставка ${item.shop.deliveryTime}", color = C.muted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}