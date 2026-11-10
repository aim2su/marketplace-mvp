package com.rebuket.mvp.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rebuket.mvp.R
import com.rebuket.mvp.core.toast.LocalToast
import com.rebuket.mvp.core.toast.ToastController
import com.rebuket.mvp.ui.cart.CartScreen
import com.rebuket.mvp.ui.cart.CartViewModel
import com.rebuket.mvp.ui.catalog.CatalogScreen
import com.rebuket.mvp.ui.checkout.CheckoutScreen
import com.rebuket.mvp.ui.components.HeaderViewModel
import com.rebuket.mvp.ui.components.MainTopBar
import com.rebuket.mvp.ui.components.RebuketBottomBar
import com.rebuket.mvp.ui.components.ToastHost
import com.rebuket.mvp.ui.constructor.ConstructorScreen
import com.rebuket.mvp.ui.eco.EcoPublishScreen
import com.rebuket.mvp.ui.eco.EcoScreen
import com.rebuket.mvp.ui.favorites.FavoritesScreen
import com.rebuket.mvp.ui.feedback.FeedbackScreen
import com.rebuket.mvp.ui.home.HomeScreen
import com.rebuket.mvp.ui.login.LoginScreen
import com.rebuket.mvp.ui.orders.OrdersScreen
import com.rebuket.mvp.ui.placeholder.PlaceholderScreen
import com.rebuket.mvp.ui.product.ProductScreen
import com.rebuket.mvp.ui.profile.BecomeCourierScreen
import com.rebuket.mvp.ui.profile.BecomeSellerScreen
import com.rebuket.mvp.ui.profile.ProfileScreen
import com.rebuket.mvp.ui.search.SearchOverlay
import com.rebuket.mvp.ui.settings.AboutScreen
import com.rebuket.mvp.ui.settings.AddressesScreen
import com.rebuket.mvp.ui.settings.FontScreen
import com.rebuket.mvp.ui.settings.LanguageScreen
import com.rebuket.mvp.ui.settings.PaymentScreen
import com.rebuket.mvp.ui.settings.ReferralScreen
import com.rebuket.mvp.ui.settings.ThemeScreen
import com.rebuket.mvp.ui.shop.ShopScreen
import com.rebuket.mvp.ui.shops.ShopsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RebuketNavHost() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val toastController = remember(scope) { ToastController(scope) }
    val headerViewModel: HeaderViewModel = hiltViewModel()
    val cartViewModel: CartViewModel = hiltViewModel()
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val cartCount = cartItems.sumOf { it.quantity }
    var showSearch by remember { mutableStateOf(false) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val mainRoutes = listOf(
        HomeRoute::class, CatalogRoute::class, ShopsRoute::class, EcoRoute::class,
        ConstructorRoute::class, CartRoute::class, ProfileRoute::class
    )
    val showTopBar = currentDest?.let { dest -> mainRoutes.any { dest.hasRoute(it) } } ?: true

    CompositionLocalProvider(LocalToast provides toastController) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    if (showTopBar) {
                        MainTopBar(
                            navController = navController,
                            viewModel = headerViewModel,
                            scrollBehavior = scrollBehavior
                        )
                    }
                },
                bottomBar = { RebuketBottomBar(navController = navController, cartCount = cartCount) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = HomeRoute,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<HomeRoute> {
                        HomeScreen(
                            onProductClick = { id -> navController.navigate(ProductRoute(id)) },
                            onShopClick = { id -> navController.navigate(ShopRoute(id)) },
                            onCatalogClick = { navController.navigate(CatalogRoute(0)) { launchSingleTop = true } },
                            onShopsClick = { navController.navigate(CatalogRoute(1)) { launchSingleTop = true } },
                            onConstructorClick = { navController.navigate(ConstructorRoute) { launchSingleTop = true } },
                            onSearchClick = { showSearch = true }
                        )
                    }
                    composable<CatalogRoute> { entry ->
                        val route = entry.toRoute<CatalogRoute>()
                        CatalogScreen(
                            onProductClick = { id -> navController.navigate(ProductRoute(id)) },
                            onConstructorClick = { navController.navigate(ConstructorRoute) },
                            onShopClick = { id -> navController.navigate(ShopRoute(id)) },
                            startTab = route.startTab
                        )
                    }
                    composable<ShopsRoute> {
                        ShopsScreen(onShopClick = { id -> navController.navigate(ShopRoute(id)) })
                    }
                    composable<ConstructorRoute> {
                        ConstructorScreen(onSuccess = { navController.navigate(OrdersRoute) })
                    }
                    composable<ProductRoute> {
                        ProductScreen(
                            onBack = { navController.popBackStack() },
                            onSellerClick = { shopId -> navController.navigate(ShopRoute(shopId)) }
                        )
                    }
                    composable<ShopRoute> {
                        ShopScreen(
                            onBack = { navController.popBackStack() },
                            onProductClick = { id -> navController.navigate(ProductRoute(id)) }
                        )
                    }
                    composable<EcoRoute> {
                        EcoScreen(onPublishClick = { navController.navigate(EcoPublishRoute) })
                    }
                    composable<EcoPublishRoute> {
                        EcoPublishScreen(onBack = { navController.popBackStack() })
                    }
                    composable<CartRoute> {
                        CartScreen(
                            onCheckout = { navController.navigate(CheckoutRoute) },
                            onProductClick = { id -> navController.navigate(ProductRoute(id)) },
                            onOpenCatalog = { navController.navigate(CatalogRoute(0)) { launchSingleTop = true } }
                        )
                    }
                    composable<CheckoutRoute> {
                        CheckoutScreen(onDone = { navController.popBackStack() })
                    }
                    composable<OrdersRoute> {
                        OrdersScreen(
                            onBack = { navController.popBackStack() },
                            onProductClick = { id -> navController.navigate(ProductRoute(id)) }
                        )
                    }
                    composable<ProfileRoute> {
                        ProfileScreen(
                            onNavigateToLogin = { navController.navigate(LoginRoute) { launchSingleTop = true } },
                            onNavigateToOrders = { navController.navigate(OrdersRoute) },
                            onNavigateToFavorites = { navController.navigate(FavoritesRoute) },
                            onNavigateToFeedback = { navController.navigate(FeedbackRoute) { launchSingleTop = true } },
                            onNavigateToEco = { navController.navigate(EcoRoute) { launchSingleTop = true } },
                            onNavigateToSeller = { navController.navigate(BecomeSellerRoute) { launchSingleTop = true } },
                            onNavigateToCourier = { navController.navigate(BecomeCourierRoute) { launchSingleTop = true } },
                            onNavigateToTheme = { navController.navigate(ThemeRoute) },
                            onNavigateToFont = { navController.navigate(FontRoute) },
                            onNavigateToLanguage = { navController.navigate(LanguageRoute) },
                            onNavigateToAddresses = { navController.navigate(AddressesRoute) },
                            onNavigateToPayment = { navController.navigate(PaymentRoute) },
                            onNavigateToReferral = { navController.navigate(ReferralRoute) },
                            onNavigateToAbout = { navController.navigate(AboutRoute) }
                        )
                    }
                    composable<ThemeRoute> { ThemeScreen(onBack = { navController.popBackStack() }) }
                    composable<FontRoute> { FontScreen(onBack = { navController.popBackStack() }) }
                    composable<LanguageRoute> { LanguageScreen(onBack = { navController.popBackStack() }) }
                    composable<AddressesRoute> { AddressesScreen(onBack = { navController.popBackStack() }) }
                    composable<PaymentRoute> { PaymentScreen(onBack = { navController.popBackStack() }) }
                    composable<ReferralRoute> { ReferralScreen(onBack = { navController.popBackStack() }) }
                    composable<AboutRoute> { AboutScreen(onBack = { navController.popBackStack() }) }
                    composable<FavoritesRoute> {
                        FavoritesScreen(onProductClick = { id -> navController.navigate(ProductRoute(id)) })
                    }
                    composable<LoginRoute> {
                        LoginScreen(
                            onBack = { navController.popBackStack() },
                            onSaved = { navController.popBackStack() }
                        )
                    }
                    composable<FeedbackRoute> {
                        FeedbackScreen(onBack = { navController.popBackStack() })
                    }
                    composable<BecomeSellerRoute> {
                        BecomeSellerScreen(onBack = { navController.popBackStack() })
                    }
                    composable<BecomeCourierRoute> {
                        BecomeCourierScreen(onBack = { navController.popBackStack() })
                    }
                    composable<ReviewsRoute> {
                        PlaceholderScreen(
                            title = stringResource(R.string.reviews_menu),
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }

            if (showSearch) {
                SearchOverlay(
                    onClose = { showSearch = false },
                    onOpenProduct = { id ->
                        showSearch = false
                        navController.navigate(ProductRoute(id))
                    }
                )
            }
            ToastHost()
        }
    }
}