package com.rebuket.mvp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebuket.mvp.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val name: String = "",
    val phone: String = "",
    val avatar: String = "",
    val birthYear: String = "",
    val gender: String = ""
) {
    val isLoggedIn: Boolean get() = name.isNotBlank() && phone.isNotBlank()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(private val prefs: UserPreferencesRepository) : ViewModel() {
    val state: StateFlow<ProfileState> = combine(
        prefs.userName, prefs.userPhone, prefs.avatar, prefs.birthYear, prefs.gender
    ) { n, p, a, b, g -> ProfileState(n, p, a, b, g) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileState())

    fun logout() { viewModelScope.launch { prefs.clearProfile() } }
}