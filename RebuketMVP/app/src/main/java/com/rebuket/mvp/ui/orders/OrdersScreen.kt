package com.rebuket.mvp.ui.orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocalAppLocale
import com.rebuket.mvp.ui.components.ReviewDialog
import com.rebuket.mvp.ui.components.getProductImages
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onProductClick: (Long) -> Unit
) {
    val ordersWithProducts by viewModel.ordersWithProducts.collectAsStateWithLifecycle()
    val locale = LocalAppLocale.current
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    var reviewSellerId by remember { mutableStateOf<Long?>(null) }

    Scaffold(topBar = {
        TopAppBar(
            windowInsets = WindowInsets(0),
            title = { Text(stringResource(R.string.orders_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            }
        )
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (ordersWithProducts.isEmpty()) {
                Text(stringResource(R.string.orders_empty))
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(ordersWithProducts, key = { it.order.id }) { data ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(stringResource(R.string.order_number_format, data.order.id),
                                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(data.order.status, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(sdf.format(Date(data.order.timestamp)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            if (data.products.isNotEmpty()) {
                                Spacer(Modifier.height(8.dp))
                                Text(stringResource(R.string.order_products_label), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.height(4.dp))
                                data.products.forEach { product ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().clickable { onProductClick(product.id) }.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val images = getProductImages(product.id)
                                        Image(
                                            painter = painterResource(id = images.first()),
                                            contentDescription = null,
                                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Column(modifier = Modifier.padding(start = 8.dp).weight(1f)) {
                                            Text(product.getLocalizedName(locale), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                            Text(stringResource(R.string.price_format, product.price.toInt()), color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                    OutlinedButton(
                                        onClick = { reviewSellerId = product.shopId },
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                    ) {
                                        Text(stringResource(R.string.review_for_seller), style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(stringResource(R.string.total_label), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(stringResource(R.string.price_format, data.order.totalPrice.toInt()),
                                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }

    reviewSellerId?.let { sellerId ->
        ReviewDialog(
            onDismiss = { reviewSellerId = null },
            onSubmit = { rating, comment ->
                viewModel.submitReview(sellerId, rating, comment)
                reviewSellerId = null
            }
        )
    }
}