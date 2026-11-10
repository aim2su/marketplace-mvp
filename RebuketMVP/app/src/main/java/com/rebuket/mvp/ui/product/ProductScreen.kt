package com.rebuket.mvp.ui.product

import androidx.compose.material.icons.filled.Remove

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocalAppLocale
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.ui.components.ImageSlider
import com.rebuket.mvp.ui.components.ReviewDialog
import com.rebuket.mvp.ui.components.ShopLogo
import com.rebuket.mvp.ui.components.getProductImages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSellerClick: (Long) -> Unit
) {
    val C = LocalExtendedPalette.current
    val locale = LocalAppLocale.current
    val product by viewModel.product.collectAsStateWithLifecycle()
    val seller by viewModel.seller.collectAsStateWithLifecycle()
    val qty by viewModel.quantityInCart.collectAsStateWithLifecycle()
    var showReview by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.product_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = C.surface,
                    titleContentColor = C.text,
                    navigationIconContentColor = C.text
                )
            )
        },
        bottomBar = {
            product?.let { p ->
                Row(
                    modifier = Modifier.fillMaxWidth().background(C.surface).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.product_total_label), color = C.muted, fontSize = 10.sp)
                        Text("${(p.price * (if (qty > 0) qty else 1)).toInt()} TJS", color = C.text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    if (qty > 0) {
                        Row(
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(C.bg).padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(C.surface).clickable { viewModel.decrement(qty) }, contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Remove, contentDescription = null, tint = C.text)
                            }
                            Text("$qty", color = C.text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(C.primary).clickable { viewModel.increment(qty) }, contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(if (p.inStock) C.primary else C.muted).clickable(enabled = p.inStock) { viewModel.addToCart() }.padding(horizontal = 28.dp, vertical = 14.dp)
                        ) {
                            Text(stringResource(R.string.product_add_to_cart_btn), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    ) { padding ->
        val p = product
        if (p == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding))
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
                Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                    ImageSlider(images = getProductImages(p.id), modifier = Modifier.fillMaxSize())
                    IconButton(
                        onClick = { viewModel.toggleFavorite(p) },
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.94f))
                    ) {
                        Icon(
                            if (p.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = if (p.isFavorite) C.danger else C.text,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (p.getDiscountPercent() > 0) {
                        Box(
                            modifier = Modifier.align(Alignment.TopStart).padding(10.dp).clip(RoundedCornerShape(8.dp)).background(C.danger).padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("−${p.getDiscountPercent()}%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(p.category.uppercase(), color = C.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(p.getLocalizedName(locale), color = C.text, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 10.dp)) {
                        if (p.rating > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = C.amber, modifier = Modifier.size(14.dp))
                                Text(String.format("%.1f", p.rating), color = C.text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("(${p.reviewCount})", color = C.muted, fontSize = 12.sp)
                            }
                        }
                        if (p.deliveryHours != null) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(50)).background(C.primaryLight).padding(horizontal = 10.dp, vertical = 4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) { Icon(Icons.Filled.Schedule, contentDescription = null, tint = C.primary, modifier = Modifier.size(11.dp)); Text("${p.deliveryHours} ч", color = C.primary, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 16.dp)) {
                        Text("${p.price.toInt()} TJS", color = C.primary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        if (p.oldPrice != null) {
                            Text("${p.oldPrice.toInt()} TJS", color = C.muted, fontSize = 15.sp, textDecoration = TextDecoration.LineThrough)
                        }
                    }
                    Text(p.description, color = C.text, fontSize = 14.sp, lineHeight = 22.sp, modifier = Modifier.padding(top = 16.dp))

                    Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp).clip(RoundedCornerShape(14.dp)).background(C.surface).padding(14.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoLine(stringResource(R.string.product_confirm_15min))
                            InfoLine(stringResource(R.string.product_confirm_photo))
                            InfoLine(stringResource(R.string.product_confirm_pickup))
                        }
                    }

                    seller?.let { s ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp).clip(RoundedCornerShape(14.dp)).background(C.surface).clickable { onSellerClick(s.shop.id) }.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ShopLogo(logoRes = s.shop.logoRes, size = 48.dp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(stringResource(R.string.product_seller_label).uppercase(), color = C.muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(s.shop.getLocalizedName(locale), color = C.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Filled.Star, contentDescription = null, tint = C.amber, modifier = Modifier.size(10.dp))
                                    Text(String.format("%.1f", s.avgRating ?: 0.0), color = C.muted, fontSize = 11.sp)
                                }
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = C.muted, modifier = Modifier.size(18.dp))
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp).clip(RoundedCornerShape(14.dp)).background(C.surface).clickable { showReview = true }.padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Filled.Email, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
                            Text(stringResource(R.string.product_review_cta), color = C.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }

    if (showReview) {
        ReviewDialog(
            onDismiss = { showReview = false },
            onSubmit = { rating, comment ->
                viewModel.submitReview(rating, comment)
                showReview = false
            }
        )
    }
}

@Composable
private fun InfoLine(text: String) {
    val C = LocalExtendedPalette.current
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(Icons.Filled.Star, contentDescription = null, tint = C.primary, modifier = Modifier.size(14.dp))
        Text(text, color = C.text, fontSize = 12.sp)
    }
}