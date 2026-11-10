package com.rebuket.mvp.ui.catalog

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocalAppLocale
import com.rebuket.mvp.ui.components.ProductCard

@Composable
fun CatalogScreen(
    onProductClick: (Long) -> Unit,
    onConstructorClick: () -> Unit,
    onShopClick: (Long) -> Unit,
    startTab: Int = 0
) {
    val C = com.rebuket.mvp.core.theme.LocalExtendedPalette.current
    var currentTab by remember { mutableStateOf(startTab) }
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = currentTab,
            containerColor = C.surface,
            contentColor = C.primary
        ) {
            Tab(
                selected = currentTab == 0,
                onClick = { currentTab = 0 },
                text = { Text(stringResource(R.string.tab_products), fontWeight = if (currentTab == 0) FontWeight.Bold else FontWeight.Normal, color = if (currentTab == 0) C.primary else C.muted) }
            )
            Tab(
                selected = currentTab == 1,
                onClick = { currentTab = 1 },
                text = { Text(stringResource(R.string.tab_shops), fontWeight = if (currentTab == 1) FontWeight.Bold else FontWeight.Normal, color = if (currentTab == 1) C.primary else C.muted) }
            )
        }
        if (currentTab == 0) {
            ProductsTab(onProductClick = onProductClick, onConstructorClick = onConstructorClick)
        } else {
            com.rebuket.mvp.ui.shops.ShopsScreen(onShopClick = onShopClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductsTab(
    onProductClick: (Long) -> Unit,
    onConstructorClick: () -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val cartQuantities by viewModel.cartQuantities.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isFilterActive by viewModel.isFilterActive.collectAsStateWithLifecycle()
    val locale = LocalAppLocale.current
    var showFilters by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState.isScrollInProgress) {
        if (gridState.isScrollInProgress) focusManager.clearFocus()
    }

    Box(
        modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    label = { Text(stringResource(R.string.search_hint)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch(); focusManager.clearFocus() }) {
                                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.clear_search))
                            }
                        }
                    }
                )
                IconButton(onClick = { showFilters = true }, modifier = Modifier.padding(start = 8.dp)) {
                    Icon(Icons.Default.Tune, contentDescription = stringResource(R.string.filters), tint = MaterialTheme.colorScheme.primary)
                }
            }

            if (isFilterActive) {
                TextButton(onClick = { viewModel.resetFilters() }, modifier = Modifier.padding(top = 4.dp)) {
                    Text(stringResource(R.string.reset_all))
                }
            }

            if (products.isEmpty() && (searchQuery.isNotEmpty() || isFilterActive)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(64.dp).height(64.dp))
                    Text(stringResource(R.string.search_empty_message), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    TextButton(onClick = { viewModel.resetAll(); focusManager.clearFocus() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text(stringResource(R.string.reset_all))
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize().padding(top = 8.dp)
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
        ExtendedFloatingActionButton(
            onClick = onConstructorClick,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
            text = { Text(stringResource(R.string.collect_bouquet)) }
        )
    }

    if (showFilters) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showFilters = false },
            sheetState = sheetState,
            contentWindowInsets = { WindowInsets(0) }
        ) {
            FilterSheetContent(viewModel) { showFilters = false }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSheetContent(viewModel: CatalogViewModel, onDismiss: () -> Unit) {
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
    val minPrice by viewModel.minPrice.collectAsStateWithLifecycle()
    val maxPrice by viewModel.maxPrice.collectAsStateWithLifecycle()
    val selectedShopId by viewModel.selectedShopId.collectAsStateWithLifecycle()
    val shops by viewModel.shops.collectAsStateWithLifecycle()
    val locale = LocalAppLocale.current

    Column(modifier = Modifier.fillMaxHeight(0.9f).padding(16.dp)) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(stringResource(R.string.sort_by), style = MaterialTheme.typography.titleMedium)
            var sortExpanded by remember { mutableStateOf(false) }
            val sortText = when (sortOrder) {
                SortOrder.DEFAULT -> stringResource(R.string.sort_default)
                SortOrder.PRICE_ASC -> stringResource(R.string.sort_price_asc)
                SortOrder.PRICE_DESC -> stringResource(R.string.sort_price_desc)
            }
            ExposedDropdownMenuBox(expanded = sortExpanded, onExpandedChange = { sortExpanded = !sortExpanded }) {
                OutlinedTextField(
                    value = sortText, onValueChange = {}, readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth().padding(top = 8.dp)
                )
                ExposedDropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
                    DropdownMenuItem(text = { Text(stringResource(R.string.sort_default)) }, onClick = { viewModel.setSortOrder(SortOrder.DEFAULT); sortExpanded = false })
                    DropdownMenuItem(text = { Text(stringResource(R.string.sort_price_asc)) }, onClick = { viewModel.setSortOrder(SortOrder.PRICE_ASC); sortExpanded = false })
                    DropdownMenuItem(text = { Text(stringResource(R.string.sort_price_desc)) }, onClick = { viewModel.setSortOrder(SortOrder.PRICE_DESC); sortExpanded = false })
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(stringResource(R.string.price_range, 0, 0).substringBefore(":"), style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = minPrice,
                    onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.setMinPrice(it) },
                    label = { Text("От") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f), singleLine = true
                )
                OutlinedTextField(
                    value = maxPrice,
                    onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.setMaxPrice(it) },
                    label = { Text("До") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f), singleLine = true
                )
            }

            Spacer(Modifier.height(24.dp))
            Text(stringResource(R.string.shop_filter), style = MaterialTheme.typography.titleMedium)
            var shopExpanded by remember { mutableStateOf(false) }
            val shopText = if (selectedShopId == null) stringResource(R.string.all_shops) else shops.find { it.shop.id == selectedShopId }?.shop?.getLocalizedName(locale) ?: stringResource(R.string.all_shops)
            ExposedDropdownMenuBox(expanded = shopExpanded, onExpandedChange = { shopExpanded = !shopExpanded }) {
                OutlinedTextField(
                    value = shopText, onValueChange = {}, readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = shopExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth().padding(top = 8.dp)
                )
                ExposedDropdownMenu(expanded = shopExpanded, onDismissRequest = { shopExpanded = false }) {
                    DropdownMenuItem(text = { Text(stringResource(R.string.all_shops)) }, onClick = { viewModel.setShopFilter(null); shopExpanded = false })
                    shops.forEach { item ->
                        DropdownMenuItem(text = { Text(item.shop.getLocalizedName(locale)) }, onClick = { viewModel.setShopFilter(item.shop.id); shopExpanded = false })
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.cancel)) }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.apply)) }
        }
    }
}