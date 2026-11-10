package com.rebuket.mvp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rebuket.mvp.R
import com.rebuket.mvp.core.navigation.CartRoute
import com.rebuket.mvp.core.navigation.CatalogRoute
import com.rebuket.mvp.core.navigation.CheckoutRoute
import com.rebuket.mvp.core.navigation.ConstructorRoute
import com.rebuket.mvp.core.navigation.EcoPublishRoute
import com.rebuket.mvp.core.navigation.EcoRoute
import com.rebuket.mvp.core.navigation.FavoritesRoute
import com.rebuket.mvp.core.navigation.HomeRoute
import com.rebuket.mvp.core.navigation.OrdersRoute
import com.rebuket.mvp.core.navigation.ProductRoute
import com.rebuket.mvp.core.navigation.ProfileRoute
import com.rebuket.mvp.core.navigation.SettingsRoute
import com.rebuket.mvp.core.navigation.ShopRoute
import com.rebuket.mvp.core.navigation.ShopsRoute
import com.rebuket.mvp.core.theme.LocalExtendedPalette

data class BottomNavItem(val route: Any, val icon: ImageVector, val labelRes: Int)

val bottomNavItems = listOf(
    BottomNavItem(HomeRoute, Icons.Filled.Home, R.string.tab_home),
    BottomNavItem(CatalogRoute(0), Icons.Filled.Search, R.string.tab_catalog),
    BottomNavItem(EcoRoute, Icons.Filled.Eco, R.string.tab_eco),
    BottomNavItem(ConstructorRoute, Icons.Filled.Palette, R.string.tab_constructor),
    BottomNavItem(CartRoute, Icons.Filled.ShoppingCart, R.string.tab_cart),
    BottomNavItem(ProfileRoute, Icons.Filled.Person, R.string.tab_profile)
)

private fun NavDestination?.parentTab(): Int? {
    if (this == null) return null
    return when {
        runCatching { hasRoute(HomeRoute::class) }.getOrDefault(false) -> 0
        runCatching { hasRoute(CatalogRoute::class) }.getOrDefault(false) -> 1
        runCatching { hasRoute(ProductRoute::class) }.getOrDefault(false) -> 1
        runCatching { hasRoute(ShopsRoute::class) }.getOrDefault(false) -> 1
        runCatching { hasRoute(ShopRoute::class) }.getOrDefault(false) -> 1
        runCatching { hasRoute(EcoRoute::class) }.getOrDefault(false) -> 2
        runCatching { hasRoute(EcoPublishRoute::class) }.getOrDefault(false) -> 2
        runCatching { hasRoute(ConstructorRoute::class) }.getOrDefault(false) -> 3
        runCatching { hasRoute(CartRoute::class) }.getOrDefault(false) -> 4
        runCatching { hasRoute(CheckoutRoute::class) }.getOrDefault(false) -> 4
        runCatching { hasRoute(ProfileRoute::class) }.getOrDefault(false) -> 5
        runCatching { hasRoute(OrdersRoute::class) }.getOrDefault(false) -> 5
        runCatching { hasRoute(SettingsRoute::class) }.getOrDefault(false) -> 5
        runCatching { hasRoute(FavoritesRoute::class) }.getOrDefault(false) -> 5
        else -> null
    }
}

@Composable
fun RebuketBottomBar(navController: NavHostController, cartCount: Int = 0) {
    val C = LocalExtendedPalette.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(currentDest) {
        selectedIndex = currentDest.parentTab()
    }

    NavigationBar(
        containerColor = C.surface,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth().height(64.dp),
        windowInsets = WindowInsets(0)
    ) {
        bottomNavItems.forEachIndexed { idx, item ->
            val selected = idx == selectedIndex
            val isCart = item.route is CartRoute
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(HomeRoute) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Box {
                        Icon(item.icon, contentDescription = stringResource(item.labelRes), modifier = Modifier.size(20.dp))
                        if (isCart && cartCount > 0) {
                            Box(
                                modifier = Modifier.align(Alignment.TopEnd).offset(x = 8.dp, y = (-6).dp)
                                    .clip(CircleShape).background(C.danger).padding(horizontal = 4.dp, vertical = 1.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (cartCount > 99) "99+" else "$cartCount", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                label = {
                    Text(
                        stringResource(item.labelRes),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Clip
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = C.primary,
                    selectedTextColor = C.primary,
                    indicatorColor = C.primaryLight,
                    unselectedIconColor = C.muted,
                    unselectedTextColor = C.muted
                )
            )
        }
    }
}