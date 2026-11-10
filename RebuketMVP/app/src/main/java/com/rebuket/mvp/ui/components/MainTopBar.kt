package com.rebuket.mvp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rebuket.mvp.R
import com.rebuket.mvp.core.navigation.CartRoute
import com.rebuket.mvp.core.navigation.CatalogRoute
import com.rebuket.mvp.core.navigation.ConstructorRoute
import com.rebuket.mvp.core.navigation.EcoRoute
import com.rebuket.mvp.core.navigation.FavoritesRoute
import com.rebuket.mvp.core.navigation.HomeRoute
import com.rebuket.mvp.core.navigation.ProfileRoute
import com.rebuket.mvp.core.navigation.ShopsRoute
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.core.theme.PALETTES
import com.rebuket.mvp.data.preferences.UserPreferencesRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

val CITIES_LIST = listOf("Душанбе", "Худжанд", "Куляб", "Бохтар", "Истаравшан")

data class NotificationItem(val title: String, val desc: String, val time: String, val unread: Boolean)

val NOTIFICATIONS_LIST = listOf(
    NotificationItem("Заказ подтверждён", "«Настроение» принял заказ", "5 минут назад", true),
    NotificationItem("Фото готово", "Букет собран, смотрите фото", "2 часа назад", true),
    NotificationItem("Промокод REBUKET10", "Скидка 10% на следующий заказ", "вчера", false)
)

@HiltViewModel
class HeaderViewModel @Inject constructor(
    private val prefs: UserPreferencesRepository
) : ViewModel() {
    val city: StateFlow<String> = prefs.city.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Душанбе")
    val paletteKey: StateFlow<String> = prefs.paletteKey.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "classic")
    fun setCity(city: String) { viewModelScope.launch { prefs.setCity(city) } }
    fun setPalette(key: String) { viewModelScope.launch { prefs.setPaletteKey(key) } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    navController: NavHostController,
    viewModel: HeaderViewModel,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val C = LocalExtendedPalette.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val current = backStackEntry?.destination
    val city by viewModel.city.collectAsStateWithLifecycle()
    val paletteKey by viewModel.paletteKey.collectAsStateWithLifecycle()

    val screenTitle = when {
        current?.hasRoute(HomeRoute::class) == true -> stringResource(R.string.tab_home)
        current?.hasRoute(ConstructorRoute::class) == true -> stringResource(R.string.constructor_title)
        current?.hasRoute(CatalogRoute::class) == true -> stringResource(R.string.catalog_title)
        current?.hasRoute(ShopsRoute::class) == true -> stringResource(R.string.shops_title)
        current?.hasRoute(EcoRoute::class) == true -> stringResource(R.string.eco_title)
        current?.hasRoute(CartRoute::class) == true -> stringResource(R.string.cart_title)
        current?.hasRoute(FavoritesRoute::class) == true -> stringResource(R.string.favorites_title)
        current?.hasRoute(ProfileRoute::class) == true -> stringResource(R.string.profile_title)
        else -> stringResource(R.string.app_name)
    }

    var showNotifications by remember { mutableStateOf(false) }
    var showCity by remember { mutableStateOf(false) }
    var showPalette by remember { mutableStateOf(false) }
    val unreadCount = NOTIFICATIONS_LIST.count { it.unread }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_splash_leaf),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp).clip(RoundedCornerShape(6.dp))
                )
                Column {
                    Text(text = screenTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = C.text)
                }
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = C.surface,
            scrolledContainerColor = C.surface,
            titleContentColor = C.text,
            actionIconContentColor = C.text
        ),
        actions = {
            Box {
                Surface(shape = RoundedCornerShape(10.dp), color = C.bg, modifier = Modifier.clickable { showPalette = true }) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Palette, contentDescription = null, tint = C.text, modifier = Modifier.size(14.dp))
                    }
                }
                DropdownMenu(expanded = showPalette, onDismissRequest = { showPalette = false }) {
                    PALETTES.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p.name, fontWeight = if (p.key == paletteKey) FontWeight.Bold else FontWeight.Normal, color = if (p.key == paletteKey) C.primary else C.text) },
                            onClick = { viewModel.setPalette(p.key); showPalette = false }
                        )
                    }
                }
            }

            Box {
                IconButton(onClick = { showNotifications = true }) {
                    Box {
                        Icon(Icons.Outlined.Notifications, contentDescription = null)
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier.align(Alignment.TopEnd).size(8.dp).clip(RoundedCornerShape(50)).background(C.danger)
                            )
                        }
                    }
                }
                DropdownMenu(
                    expanded = showNotifications,
                    onDismissRequest = { showNotifications = false },
                    modifier = Modifier.widthIn(min = 280.dp)
                ) {
                    NOTIFICATIONS_LIST.forEach { n ->
                        DropdownMenuItem(
                            text = {
                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        if (n.unread) {
                                            Box(modifier = Modifier.size(6.dp).clip(RoundedCornerShape(50)).background(C.primary))
                                        }
                                        Text(n.title, color = C.text, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                    Text(n.desc, color = C.muted, fontSize = 11.sp)
                                    Text(n.time, color = C.muted, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
                                }
                            },
                            onClick = { showNotifications = false }
                        )
                    }
                }
            }

            Box {
                Surface(shape = RoundedCornerShape(10.dp), color = C.bg, modifier = Modifier.clickable { showCity = true }) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = C.text, modifier = Modifier.size(14.dp))
                        Text(city, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = C.text)
                    }
                }
                DropdownMenu(expanded = showCity, onDismissRequest = { showCity = false }, modifier = Modifier.widthIn(min = 180.dp)) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.choose_city), fontWeight = FontWeight.Bold, color = C.muted, fontSize = 11.sp) },
                        onClick = {},
                        enabled = false
                    )
                    CITIES_LIST.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c, fontWeight = if (c == city) FontWeight.Bold else FontWeight.Normal, color = if (c == city) C.primary else C.text) },
                            onClick = { viewModel.setCity(c); showCity = false }
                        )
                    }
                }
            }
        }
    )
}