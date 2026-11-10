package com.rebuket.mvp.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.rebuket.mvp.ui.components.getProductImages

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onCheckout: () -> Unit,
    onProductClick: (Long) -> Unit,
    onOpenCatalog: () -> Unit
) {
    val C = LocalExtendedPalette.current
    val locale = LocalAppLocale.current
    val items by viewModel.cartItems.collectAsStateWithLifecycle()
    val total by viewModel.totalPrice.collectAsStateWithLifecycle()

    if (items.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(110.dp).clip(CircleShape).background(C.primaryLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = null, tint = C.primary, modifier = Modifier.size(54.dp))
            }
            Text(
                stringResource(R.string.cart_empty_title),
                color = C.text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                stringResource(R.string.cart_empty_desc),
                color = C.muted,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 6.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(C.primary)
                    .clickable { onOpenCatalog() }
                    .padding(horizontal = 28.dp, vertical = 14.dp)
            ) {
                Text(stringResource(R.string.select_product), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                "${items.sumOf { it.quantity }} шт.",
                color = C.muted,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp)
            )
            LazyColumn(
                modifier = Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items, key = { it.product.id }) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(C.surface)
                            .clickable { onProductClick(item.product.id) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = getProductImages(item.product.id).first()),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(10.dp))
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                item.product.getLocalizedName(locale),
                                color = C.text,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.size(26.dp).clip(RoundedCornerShape(6.dp)).background(C.bg).clickable { viewModel.decrement(item.product.id, item.quantity) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Filled.Remove, contentDescription = null, tint = C.text, modifier = Modifier.size(14.dp))
                                    }
                                    Text("${item.quantity}", color = C.text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Box(
                                        modifier = Modifier.size(26.dp).clip(RoundedCornerShape(6.dp)).background(C.primary).clickable { viewModel.increment(item.product.id, item.quantity) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                }
                                Text(
                                    "${(item.product.price * item.quantity).toInt()} TJS",
                                    color = C.text,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.size(24.dp).clip(CircleShape).clickable { viewModel.remove(item.product.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = null, tint = C.muted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().background(C.surface).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(stringResource(R.string.total_label), color = C.muted, fontSize = 11.sp)
                    Text("${total.toInt()} TJS", color = C.text, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(C.primary).clickable { onCheckout() }.padding(horizontal = 28.dp, vertical = 14.dp)
                ) {
                    Text(stringResource(R.string.cart_checkout), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}