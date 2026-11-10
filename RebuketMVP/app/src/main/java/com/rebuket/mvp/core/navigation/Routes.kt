package com.rebuket.mvp.core.navigation
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute
@Serializable data class CatalogRoute(val startTab: Int = 0)
@Serializable data class ProductRoute(val id: Long)
@Serializable data object ShopsRoute
@Serializable data class ShopRoute(val id: Long)
@Serializable data object EcoRoute
@Serializable data object EcoPublishRoute
@Serializable data object CartRoute
@Serializable data object CheckoutRoute
@Serializable data object OrdersRoute
@Serializable data object ProfileRoute
@Serializable data object ConstructorRoute
@Serializable data object FavoritesRoute
@Serializable data object BecomeSellerRoute
@Serializable data object BecomeCourierRoute
@Serializable data object ReviewsRoute
@Serializable data object SettingsRoute
@Serializable data object LoginRoute
@Serializable data object FeedbackRoute
@Serializable data object ThemeRoute
@Serializable data object FontRoute
@Serializable data object LanguageRoute
@Serializable data object AddressesRoute
@Serializable data object PaymentRoute
@Serializable data object ReferralRoute
@Serializable data object AboutRoute