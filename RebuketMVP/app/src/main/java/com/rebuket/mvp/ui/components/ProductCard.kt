package com.rebuket.mvp.ui.components

import androidx.compose.material.icons.filled.Remove

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rebuket.mvp.data.local.entity.ProductEntity
import com.rebuket.mvp.core.theme.LocalExtendedPalette

@Composable
fun ProductCard(
    product: ProductEntity,
    quantityInCart: Int,
    locale: String,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    val C = LocalExtendedPalette.current
    val discount = product.getDiscountPercent()
    val outOfStock = !product.inStock

    Card(
        onClick = onProductClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = C.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                ImageSlider(
                    images = getProductImages(product.id),
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )

                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (product.isFavorite) C.danger else C.text,
                        modifier = Modifier.size(18.dp)
                    )
                }

                if (discount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(C.danger)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "−$discount%",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (outOfStock) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.White.copy(alpha = 0.65f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Нет в наличии",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = C.text
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    product.getLocalizedName(locale),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = C.text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                if (product.rating > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = C.amber, modifier = Modifier.size(11.dp))
                        Text(
                            text = String.format("%.1f", product.rating),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = C.text
                        )
                        if (product.reviewCount > 0) {
                            Text(
                                text = "(${product.reviewCount})",
                                fontSize = 10.sp,
                                color = C.muted
                            )
                        }
                    }
                }

                if (product.deliveryHours != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(Icons.Filled.Schedule, contentDescription = null, tint = C.primary, modifier = Modifier.size(10.dp))
                        Text(
                            text = "${product.deliveryHours} ч",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = C.primary
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${product.price.toInt()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = C.text
                            )
                            Text(
                                text = " TJS",
                                fontSize = 9.sp,
                                color = C.muted,
                                modifier = Modifier.padding(bottom = 1.dp)
                            )
                        }
                        Text(
                            text = if (product.oldPrice != null) "${product.oldPrice.toInt()} TJS" else " ",
                            fontSize = 9.sp,
                            color = if (product.oldPrice != null) C.muted else Color.Transparent,
                            textDecoration = if (product.oldPrice != null) TextDecoration.LineThrough else TextDecoration.None
                        )
                    }

                    if (quantityInCart > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(C.bg)
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(C.surface).clickable { onDecrement() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = null, tint = C.text, modifier = Modifier.size(12.dp))
                            }
                            Text(
                                text = "$quantityInCart",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = C.text,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                            Box(
                                modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).background(C.primary).clickable { onIncrement() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (outOfStock) C.muted else C.primary)
                                .clickable(enabled = !outOfStock) { onAddToCart() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}