package com.rebuket.mvp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.core.locale.LocalAppContext
import com.rebuket.mvp.core.locale.LocalAppLocale
import com.rebuket.mvp.core.locale.LocaleManager
import com.rebuket.mvp.core.navigation.RebuketNavHost
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.core.theme.RebuketTheme
import com.rebuket.mvp.core.theme.getPalette
import com.rebuket.mvp.data.preferences.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AppThemeState(
    val themeMode: String = "SYSTEM",
    val fontSize: String = "m",
    val paletteKey: String = "classic",
    val city: String = "Душанбе"
)

@HiltViewModel
class MainViewModel @Inject constructor(prefs: UserPreferencesRepository) : ViewModel() {
    val themeState: StateFlow<AppThemeState> = combine(
        prefs.themeMode,
        prefs.fontSize,
        prefs.paletteKey,
        prefs.city
    ) { mode, font, palette, city ->
        AppThemeState(mode, font, palette, city)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, AppThemeState())
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val appLocale = remember(context) { LocaleManager.currentTag(context) }
            val themeState by viewModel.themeState.collectAsStateWithLifecycle()
            val isDark = when (themeState.themeMode) {
                "LIGHT" -> false
                "DARK" -> true
                else -> isSystemInDarkTheme()
            }
            val palette = getPalette(themeState.paletteKey, isDark)

            CompositionLocalProvider(
                LocalAppLocale provides appLocale,
                LocalAppContext provides context,
                LocalExtendedPalette provides palette
            ) {
                RebuketTheme(
                    themeMode = themeState.themeMode,
                    fontSizeKey = themeState.fontSize,
                    paletteKey = themeState.paletteKey
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(palette.bg)) {
                        RebuketNavHost()
                    }
                }
            }
        }
    }
}