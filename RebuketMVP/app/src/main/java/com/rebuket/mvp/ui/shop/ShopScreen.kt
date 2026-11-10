package com.rebuket.mvp.ui.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocalAppLocale
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.core.toast.LocalToast
import com.rebuket.mvp.ui.components.ProductCard
import com.rebuket.mvp.ui.components.ReviewDialog
import com.rebuket.mvp.ui.components.ShopLogo
import com.rebuket.mvp.ui.components.StarRatingDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    viewModel: ShopViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onProductClick: (Long) -> Unit
) {
    val C = LocalExtendedPalette.current
    val toast = LocalToast.current
    val locale = LocalAppLocale.current
    val shop by viewModel.shop.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    val cartQuantities by viewModel.cartQuantities.collectAsStateWithLifecycle()
    var showReviewDialog by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0),
                title = { Text(shop?.shop?.getLocalizedName(locale) ?: stringResource(R.string.shop_title), fontWeight = FontWeight.Bold, color = C.text) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = C.text)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = C.surface)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ShopLogo(logoRes = shop?.shop?.logoRes ?: "", size = 56.dp)
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(shop?.shop?.getLocalizedName(locale) ?: "", color = C.text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            if (shop?.shop?.verified == true) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = C.primary, modifier = Modifier.size(14.dp))
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 2.dp)) {
                            if (shop?.avgRating != null && (shop?.reviewCount ?: 0) > 0) {
                                StarRatingDisplay(shop!!.avgRating ?: 0.0, starSize = 12.dp)
                                Text(String.format("%.1f", shop!!.avgRating), color = C.text, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("· ${reviews.size}", color = C.muted, fontSize = 11.sp)
                            } else {
                                Text(stringResource(R.string.no_ratings), color = C.muted, fontSize = 11.sp)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 2.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(androidx.compose.foundation.shape.CircleShape).background(if (shop?.shop?.isOpen == true) Color(0xFF22C55E) else C.muted))
                            Text(
                                stringResource(if (shop?.shop?.isOpen == true) R.string.shop_open else R.string.shop_closed),
                                color = if (shop?.shop?.isOpen == true) C.primary else C.muted, fontSize = 10.sp, fontWeight = FontWeight.Bold
                            )
                            Text("· ${shop?.shop?.hours ?: ""}", color = C.muted, fontSize = 10.sp)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { toast.show("Скоро: чат с продавцом") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = C.primary)
                    ) {
                        Icon(Icons.Filled.Email, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Text("  " + stringResource(R.string.shop_write_btn), fontSize = 13.sp)
                    }
                    OutlinedButton(onClick = { toast.show("Скоро: звонок") }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.Phone, contentDescription = null, tint = C.primary, modifier = Modifier.size(16.dp))
                        Text("  " + stringResource(R.string.shop_call_btn), fontSize = 13.sp, color = C.primary)
                    }
                }
            }

            TabRow(
                selectedTabIndex = currentTab,
                containerColor = C.surface,
                contentColor = C.primary
            ) {
                listOf(R.string.shop_tab_products, R.string.shop_tab_reviews, R.string.shop_tab_about).forEachIndexed { idx, res ->
                    Tab(
                        selected = currentTab == idx,
                        onClick = { currentTab = idx },
                        text = {
                            Text(
                                stringResource(res),
                                fontWeight = if (currentTab == idx) FontWeight.Bold else FontWeight.Normal,
                                color = if (currentTab == idx) C.primary else C.muted,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            when (currentTab) {
                0 -> {
                    if (products.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.shop_no_products), color = C.muted)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
                        ) {
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
                        }
                    }
                }
                1 -> {
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                        Button(
                            onClick = { showReviewDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = C.primary)
                        ) {
                            Text(stringResource(R.string.review_leave_button), fontWeight = FontWeight.Bold)
                        }
                        if (reviews.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                                Text(stringResource(R.string.shop_no_reviews), color = C.muted)
                            }
                        } else {
                            reviews.forEach { r ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                    colors = CardDefaults.cardColors(containerColor = C.surface),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            StarRatingDisplay(r.rating.toDouble(), starSize = 14.dp)
                                        }
                                        if (r.comment.isNotEmpty()) {
                                            Text(r.comment, color = C.text, fontSize = 13.sp, modifier = Modifier.padding(top = 6.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                        Card(colors = CardDefaults.cardColors(containerColor = C.surface), shape = RoundedCornerShape(14.dp)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(stringResource(R.string.shop_about_desc), color = C.text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(shop?.shop?.getLocalizedDescription(locale) ?: "", color = C.text, fontSize = 13.sp, modifier = Modifier.padding(top = 6.dp))
                                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).size(1.dp).background(C.border))
                                Text(stringResource(R.string.shop_about_contacts), color = C.text, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 6.dp))
                                Text("Адрес: ${shop?.shop?.address ?: ""}", color = C.text, fontSize = 13.sp)
                                Text("Часы: ${shop?.shop?.hours ?: ""}", color = C.text, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 2.dp)) {
                                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = C.primary, modifier = Modifier.size(12.dp))
                                    Text("${shop?.shop?.deliveryTime ?: ""} · ${shop?.shop?.deliveryFee ?: 0} TJS", color = C.text, fontSize = 13.sp)
                                }
                                Text("Мин. заказ: ${shop?.shop?.minOrder ?: 0} TJS", color = C.text, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showReviewDialog) {
        ReviewDialog(
            onDismiss = { showReviewDialog = false },
            onSubmit = { rating, comment -> viewModel.submitReview(rating, comment) }
        )
    }
}