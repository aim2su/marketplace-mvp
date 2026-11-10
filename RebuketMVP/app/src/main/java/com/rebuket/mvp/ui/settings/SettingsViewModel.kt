package com.rebuket.mvp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val prefs: UserPreferencesRepository) : ViewModel() {
    val themeMode: StateFlow<String> = prefs.themeMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "SYSTEM")
    val fontSize: StateFlow<String> = prefs.fontSize.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "m")
    val paletteKey: StateFlow<String> = prefs.paletteKey.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "classic")
    val notificationsEnabled: StateFlow<Boolean> = prefs.notificationsEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setTheme(mode: String) { viewModelScope.launch { prefs.setThemeMode(mode) } }
    fun setFontSize(size: String) { viewModelScope.launch { prefs.setFontSize(size) } }
    fun setPalette(key: String) { viewModelScope.launch { prefs.setPaletteKey(key) } }
    fun setNotifications(enabled: Boolean) { viewModelScope.launch { prefs.setNotificationsEnabled(enabled) } }
}