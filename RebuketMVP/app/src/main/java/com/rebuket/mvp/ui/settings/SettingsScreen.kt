package com.rebuket.mvp.ui.settings

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocaleManager
import com.rebuket.mvp.core.theme.FONT_SIZES
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.core.toast.LocalToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel(), onBack: () -> Unit) {
    val C = LocalExtendedPalette.current
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val notifications by viewModel.notificationsEnabled.collectAsStateWithLifecycle()
    val fontSize by viewModel.fontSize.collectAsStateWithLifecycle()
    var page by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val toast = LocalToast.current

    val title = when (page) {
        "theme" -> stringResource(R.string.choose_theme)
        "font" -> stringResource(R.string.choose_font)
        "lang" -> stringResource(R.string.choose_lang)
        else -> stringResource(R.string.settings)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { if (page != null) page = null else onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = C.surface,
                    titleContentColor = C.text,
                    navigationIconContentColor = C.text
                )
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (page) {
                "theme" -> Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("LIGHT" to (R.string.theme_light to Icons.Filled.Settings), "DARK" to (R.string.theme_dark to Icons.Filled.Settings), "SYSTEM" to (R.string.theme_system to Icons.Filled.Settings)).forEach { (key, pair) ->
                        val active = key == themeMode
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (active) C.primaryLight else C.surface).clickable { viewModel.setTheme(key) }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Icon(pair.second, contentDescription = null, tint = if (active) C.primary else C.text, modifier = Modifier.size(22.dp))
                            Text(stringResource(pair.first), color = if (active) C.primary else C.text, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            if (active) Icon(Icons.Filled.Check, contentDescription = null, tint = C.primary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
                "font" -> Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FONT_SIZES.forEach { f ->
                        val active = f.key == fontSize
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (active) C.primaryLight else C.surface).clickable { viewModel.setFontSize(f.key) }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text("A", color = if (active) C.primary else C.text, fontWeight = FontWeight.Bold, fontSize = (16 * f.scale).sp)
                            Text("${f.name}", color = if (active) C.primary else C.text, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            if (active) Icon(Icons.Filled.Check, contentDescription = null, tint = C.primary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
                "lang" -> Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("ru" to "Русский", "tj" to "Тоҷикӣ", "en" to "English").forEach { (key, label) ->
                        val active = key == LocaleManager.currentTag(context)
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (active) C.primaryLight else C.surface).clickable { LocaleManager.setLocale(context, key) }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Icon(Icons.Filled.Info, contentDescription = null, tint = if (active) C.primary else C.text, modifier = Modifier.size(22.dp))
                            Text(label, color = if (active) C.primary else C.text, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            if (active) Icon(Icons.Filled.Check, contentDescription = null, tint = C.primary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
                else -> Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(vertical = 8.dp)) {
                    GroupTitle(stringResource(R.string.section_appearance))
                    Group {
                        Item(Icons.Filled.Settings, stringResource(R.string.theme), themeMode) { page = "theme" }
                        Item(Icons.Filled.Settings, stringResource(R.string.font_size_title), fontSize) { page = "font" }
                        Item(Icons.Filled.Info, stringResource(R.string.language), LocaleManager.currentTag(context).uppercase()) { page = "lang" }
                    }
                    GroupTitle(stringResource(R.string.section_settings))
                    Group {
                        SwitchItem(Icons.Filled.Notifications, stringResource(R.string.push_notifications), notifications) { viewModel.setNotifications(it) }
                    }
                    GroupTitle(stringResource(R.string.section_help))
                    Group {
                        Item(Icons.Filled.Email, stringResource(R.string.feedback), "") { toast.show("Чат с поддержкой") }
                        Item(Icons.Filled.Home, stringResource(R.string.about), "") { toast.show("ReBuket.tj · v1.0.0") }
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupTitle(text: String) {
    val C = LocalExtendedPalette.current
    Text(text.uppercase(), color = C.muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
}

@Composable
private fun Group(content: @Composable () -> Unit) {
    val C = LocalExtendedPalette.current
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp).clip(RoundedCornerShape(16.dp)).background(C.surface)) { content() }
}

@Composable
private fun Item(icon: ImageVector, label: String, value: String, onClick: () -> Unit) {
    val C = LocalExtendedPalette.current
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(C.primaryLight), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(label, color = C.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            if (value.isNotEmpty()) Text(value, color = C.muted, fontSize = 11.sp)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = C.muted, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun SwitchItem(icon: ImageVector, label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    val C = LocalExtendedPalette.current
    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(C.primaryLight), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
        }
        Text(label, color = C.text, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange, colors = SwitchDefaults.colors(checkedThumbColor = C.surface, checkedTrackColor = C.primary))
    }
}