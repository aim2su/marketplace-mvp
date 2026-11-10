package com.rebuket.mvp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.core.toast.LocalToast
import com.rebuket.mvp.ui.settings.SettingsViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToFeedback: () -> Unit,
    onNavigateToEco: () -> Unit,
    onNavigateToSeller: () -> Unit,
    onNavigateToCourier: () -> Unit,
    onNavigateToTheme: () -> Unit,
    onNavigateToFont: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToPayment: () -> Unit,
    onNavigateToReferral: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    val C = LocalExtendedPalette.current
    val toast = LocalToast.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val notifications by settingsViewModel.notificationsEnabled.collectAsStateWithLifecycle()
    var showLogout by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.profile_title), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = C.text)
                Box(
                    modifier = Modifier.padding(top = 12.dp).fillMaxWidth().clip(RoundedCornerShape(22.dp))
                        .background(Brush.linearGradient(listOf(C.primary, C.primaryDark)))
                        .clickable { onNavigateToLogin() }.padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Box(
                            modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.22f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                if (state.isLoggedIn) state.name else stringResource(R.string.profile_guest),
                                fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White,
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                if (state.isLoggedIn) state.phone else stringResource(R.string.profile_guest_hint),
                                fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f),
                                maxLines = 2, overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        item {
            SectionTitle(stringResource(R.string.section_purchases))
            ProfileGroup {
                ProfileItem(Icons.Filled.ShoppingCart, stringResource(R.string.orders_title), stringResource(R.string.my_orders_hint)) { onNavigateToOrders() }
                HorizontalDivider(color = C.border, modifier = Modifier.padding(horizontal = 14.dp))
                ProfileItem(Icons.Filled.Favorite, stringResource(R.string.favorites_title), stringResource(R.string.favorites_hint)) { onNavigateToFavorites() }
            }
        }

        item {
            SectionTitle(stringResource(R.string.section_delivery))
            ProfileGroup {
                ProfileItem(Icons.Filled.LocationOn, stringResource(R.string.saved_addresses), stringResource(R.string.addresses_hint)) { onNavigateToAddresses() }
                HorizontalDivider(color = C.border, modifier = Modifier.padding(horizontal = 14.dp))
                ProfileItem(Icons.Filled.CreditCard, stringResource(R.string.payment_methods), stringResource(R.string.payment_hint)) { onNavigateToPayment() }
            }
        }

        item {
            SectionTitle(stringResource(R.string.section_sales))
            ProfileGroup {
                ProfileItem(Icons.Filled.Eco, stringResource(R.string.my_eco), stringResource(R.string.my_eco_hint)) { onNavigateToEco() }
                HorizontalDivider(color = C.border, modifier = Modifier.padding(horizontal = 14.dp))
                ProfileItem(Icons.Filled.Home, stringResource(R.string.become_seller), stringResource(R.string.become_seller_hint)) { onNavigateToSeller() }
                HorizontalDivider(color = C.border, modifier = Modifier.padding(horizontal = 14.dp))
                ProfileItem(Icons.Filled.Send, stringResource(R.string.become_courier), stringResource(R.string.become_courier_hint)) { onNavigateToCourier() }
            }
        }

        item {
            SectionTitle(stringResource(R.string.section_help))
            ProfileGroup {
                ProfileItem(Icons.Filled.Email, stringResource(R.string.feedback), stringResource(R.string.feedback_hint)) { onNavigateToFeedback() }
                HorizontalDivider(color = C.border, modifier = Modifier.padding(horizontal = 14.dp))
                ProfileItem(Icons.Filled.Info, stringResource(R.string.about), stringResource(R.string.about_hint)) { onNavigateToAbout() }
            }
        }

        item {
            SectionTitle(stringResource(R.string.section_appearance))
            ProfileGroup {
                ProfileItem(Icons.Filled.Palette, stringResource(R.string.theme), "") { onNavigateToTheme() }
                HorizontalDivider(color = C.border, modifier = Modifier.padding(horizontal = 14.dp))
                ProfileItem(Icons.Filled.Settings, stringResource(R.string.font_size_title), "") { onNavigateToFont() }
                HorizontalDivider(color = C.border, modifier = Modifier.padding(horizontal = 14.dp))
                ProfileItem(Icons.Filled.Info, stringResource(R.string.language), "") { onNavigateToLanguage() }
            }
        }

        item {
            SectionTitle(stringResource(R.string.section_settings))
            ProfileGroup {
                ProfileItem(Icons.Filled.CardGiftcard, stringResource(R.string.invite_friend), stringResource(R.string.referral_hint)) { onNavigateToReferral() }
                HorizontalDivider(color = C.border, modifier = Modifier.padding(horizontal = 14.dp))
                NotificationToggleItem(notifications) { settingsViewModel.setNotifications(it) }
            }
        }

        if (state.isLoggedIn) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                            .clickable { showLogout = true }.padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.logout), color = C.danger, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        item {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                Text("ReBuket.tj · v1.0.0 · Душанбе", color = C.muted, fontSize = 10.sp)
            }
        }
    }

    if (showLogout) {
        AlertDialog(
            onDismissRequest = { showLogout = false },
            title = { Text(stringResource(R.string.logout_confirm), fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.logout_warning)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.logout()
                    showLogout = false
                    toast.show("Вы вышли из аккаунта")
                }) { Text(stringResource(R.string.logout), color = C.danger) }
            },
            dismissButton = {
                TextButton(onClick = { showLogout = false }) { Text(stringResource(R.string.cancel_btn)) }
            }
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    val C = LocalExtendedPalette.current
    Text(text.uppercase(), color = C.muted, fontSize = 11.sp, fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp))
}

@Composable
private fun ProfileGroup(content: @Composable () -> Unit) {
    val C = LocalExtendedPalette.current
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp)).background(C.surface)
    ) { content() }
}

@Composable
private fun ProfileItem(icon: ImageVector, label: String, hint: String, onClick: () -> Unit) {
    val C = LocalExtendedPalette.current
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(C.primaryLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = C.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            if (hint.isNotEmpty()) Text(hint, color = C.muted, fontSize = 11.sp)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = C.muted, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun NotificationToggleItem(checked: Boolean, onToggle: (Boolean) -> Unit) {
    val C = LocalExtendedPalette.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(C.primaryLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Notifications, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
        }
        Text(stringResource(R.string.push_notifications), color = C.text, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(checkedThumbColor = C.surface, checkedTrackColor = C.primary)
        )
    }
}