package com.rebuket.mvp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rebuket.mvp.R
import com.rebuket.mvp.core.navigation.BecomeCourierRoute
import com.rebuket.mvp.core.navigation.BecomeSellerRoute
import com.rebuket.mvp.core.navigation.ReviewsRoute
import com.rebuket.mvp.core.navigation.SettingsRoute
import com.rebuket.mvp.core.navigation.ShopsRoute
import com.rebuket.mvp.core.theme.LocalExtendedPalette

@Composable
fun NavigationDrawerContent(
    navController: NavHostController,
    onClose: () -> Unit
) {
    val C = LocalExtendedPalette.current
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp).fillMaxHeight(),
        windowInsets = WindowInsets(0),
        drawerContainerColor = C.surface,
        drawerContentColor = C.text
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = C.primary
            )
            Text(
                stringResource(R.string.menu_title),
                style = MaterialTheme.typography.bodyMedium,
                color = C.muted
            )
        }
        HorizontalDivider(color = C.border)
        Spacer(Modifier.height(8.dp))

        DrawerItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = null, tint = C.primary) },
            label = stringResource(R.string.shops_title),
            onClick = { onClose(); navController.navigate(ShopsRoute) { launchSingleTop = true } }
        )
        DrawerItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = null, tint = C.primary) },
            label = stringResource(R.string.become_seller),
            onClick = { onClose(); navController.navigate(BecomeSellerRoute) { launchSingleTop = true } }
        )
        DrawerItem(
            icon = { Icon(Icons.Filled.Send, contentDescription = null, tint = C.primary) },
            label = stringResource(R.string.become_courier),
            onClick = { onClose(); navController.navigate(BecomeCourierRoute) { launchSingleTop = true } }
        )
        DrawerItem(
            icon = { Icon(Icons.Filled.Star, contentDescription = null, tint = C.primary) },
            label = stringResource(R.string.reviews_menu),
            onClick = { onClose(); navController.navigate(ReviewsRoute) { launchSingleTop = true } }
        )
        DrawerItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = null, tint = C.primary) },
            label = stringResource(R.string.settings),
            onClick = { onClose(); navController.navigate(SettingsRoute) { launchSingleTop = true } }
        )
    }
}

@Composable
private fun DrawerItem(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = { Text(label, style = MaterialTheme.typography.bodyLarge) },
        leadingContent = icon
    )
}