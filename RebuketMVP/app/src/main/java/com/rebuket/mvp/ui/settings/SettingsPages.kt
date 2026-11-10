package com.rebuket.mvp.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocaleManager
import com.rebuket.mvp.core.theme.FONT_SIZES
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.core.toast.LocalToast

private val LANGUAGES = listOf(
    "ru" to "Русский",
    "tj" to "Тоҷикӣ",
    "en" to "English"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val C = LocalExtendedPalette.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
    ) { padding -> content(padding) }
}

@Composable
fun ThemeScreen(viewModel: SettingsViewModel = hiltViewModel(), onBack: () -> Unit) {
    val C = LocalExtendedPalette.current
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    SettingsScaffold(title = stringResource(R.string.choose_theme), onBack = onBack) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                Triple("LIGHT", R.string.theme_light, Icons.Filled.LightMode),
                Triple("DARK", R.string.theme_dark, Icons.Filled.DarkMode),
                Triple("SYSTEM", R.string.theme_system, Icons.Filled.Monitor)
            ).forEach { (key, labelRes, icon) ->
                val active = key == themeMode
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (active) C.primaryLight else C.surface).clickable { viewModel.setTheme(key) }.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(if (active) C.primary else C.bg), contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = if (active) Color.White else C.text, modifier = Modifier.size(22.dp))
                    }
                    Text(stringResource(labelRes), color = if (active) C.primary else C.text, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    if (active) Icon(Icons.Filled.Check, contentDescription = null, tint = C.primary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun FontScreen(viewModel: SettingsViewModel = hiltViewModel(), onBack: () -> Unit) {
    val C = LocalExtendedPalette.current
    val fontSize by viewModel.fontSize.collectAsStateWithLifecycle()
    SettingsScaffold(title = stringResource(R.string.choose_font), onBack = onBack) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FONT_SIZES.forEach { f ->
                val active = f.key == fontSize
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (active) C.primaryLight else C.surface).clickable { viewModel.setFontSize(f.key) }.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("A", color = if (active) C.primary else C.text, fontWeight = FontWeight.Bold, fontSize = (16 * f.scale).sp, modifier = Modifier.width(28.dp), textAlign = TextAlign.Center)
                    Text(f.name, color = if (active) C.primary else C.text, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    if (active) Icon(Icons.Filled.Check, contentDescription = null, tint = C.primary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun LanguageScreen(onBack: () -> Unit) {
    val C = LocalExtendedPalette.current
    val context = LocalContext.current
    SettingsScaffold(title = stringResource(R.string.choose_lang), onBack = onBack) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val current = LocaleManager.currentTag(context)
            LANGUAGES.forEach { (key, label) ->
                val active = key == current
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (active) C.primaryLight else C.surface).clickable { LocaleManager.setLocale(context, key) }.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(if (active) C.primary else C.bg), contentAlignment = Alignment.Center) {
                        Text(key.uppercase(), color = if (active) Color.White else C.text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Text(label, color = if (active) C.primary else C.text, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    if (active) Icon(Icons.Filled.Check, contentDescription = null, tint = C.primary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun AddressesScreen(onBack: () -> Unit) {
    val C = LocalExtendedPalette.current
    val toast = LocalToast.current
    SettingsScaffold(title = stringResource(R.string.saved_addresses), onBack = onBack) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                Triple("Дом", "Душанбе, ул. Рудаки 45, кв. 12", true),
                Triple("Работа", "Душанбе, пр. Сомони 5, оф. 301", false)
            ).forEach { (label, addr, def) ->
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(C.surface).padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(C.primaryLight), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
                    }
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(label, color = C.text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            if (def) {
                                Box(Modifier.clip(RoundedCornerShape(50)).background(C.primaryLight).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                    Text("По умолчанию", color = C.primary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Text(addr, color = C.muted, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
            Box(Modifier.fillMaxWidth().padding(top = 8.dp).clip(RoundedCornerShape(14.dp)).background(C.primaryLight).clickable { toast.show("Скоро: добавление адреса") }.padding(14.dp), contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
                    Text(stringResource(R.string.add_address), color = C.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun PaymentScreen(onBack: () -> Unit) {
    val C = LocalExtendedPalette.current
    val toast = LocalToast.current
    SettingsScaffold(title = stringResource(R.string.payment_methods), onBack = onBack) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Brush.linearGradient(listOf(C.primary, C.primaryDark))).padding(20.dp)
            ) {
                Column {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Korti Millī", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                        Text("VISA", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                    }
                    Text("•••• •••• •••• 4521", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 20.dp))
                    Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("AZIZA K.", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                        Text("12/26", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                    }
                }
            }
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(C.surface).padding(20.dp)) {
                Column {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(stringResource(R.string.payment_card), color = C.muted, fontSize = 12.sp)
                        Text(stringResource(R.string.payment_other), color = C.muted, fontSize = 11.sp)
                    }
                    Text("•••• •••• •••• 8914", color = C.text, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 20.dp))
                    Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("DALER R.", color = C.muted, fontSize = 11.sp)
                        Text("05/27", color = C.muted, fontSize = 11.sp)
                    }
                }
            }
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(C.primaryLight).clickable { toast.show("Скоро: добавление карты") }.padding(14.dp), contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
                    Text(stringResource(R.string.add_card), color = C.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun ReferralScreen(onBack: () -> Unit) {
    val C = LocalExtendedPalette.current
    val toast = LocalToast.current
    val code = "REBUKET-FRIEND"
    SettingsScaffold(title = stringResource(R.string.invite_friend), onBack = onBack) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Brush.linearGradient(listOf(C.primary, C.primaryDark))).padding(24.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.CardGiftcard, contentDescription = null, tint = Color.White, modifier = Modifier.size(56.dp))
                    Text("Подарите скидку другу — получите бонус себе", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 12.dp))
                    Text(stringResource(R.string.referral_desc), color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
                }
            }
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(C.surface).padding(16.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.referral_code).uppercase(), color = C.muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(code, color = C.primary, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    Row(Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(Color.Transparent).clickable { toast.show("Код скопирован") }.padding(12.dp), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.copy), color = C.primary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Box(Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(C.primary).clickable { toast.show("Скоро: поделиться") }.padding(12.dp), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.share), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val C = LocalExtendedPalette.current
    val toast = LocalToast.current
    SettingsScaffold(title = stringResource(R.string.about), onBack = onBack) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(C.surface).padding(20.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(C.primary), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Eco, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    Text("ReBuket.tj", color = C.text, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                    Text("Маркетплейс цветов и подарков", color = C.muted, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                    Text("Версия 1.0.0 (MVP)", color = C.muted, fontSize = 11.sp, modifier = Modifier.padding(top = 12.dp))
                }
            }
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(C.surface)) {
                listOf("Пользовательское соглашение", "Политика конфиденциальности", "Правила сервиса", "Помощь и FAQ").forEachIndexed { i, label ->
                    Row(
                        Modifier.fillMaxWidth().clickable { toast.show("Скоро: $label") }.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(label, color = C.text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    }
                }
            }
            Text("ReBuket · Душанбе, Таджикистан", color = C.muted, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}