package com.rebuket.mvp.ui.login

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

data class LoginState(
    val name: String = "",
    val phone: String = "",
    val avatar: String = "",
    val birthYear: String = "",
    val gender: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(private val prefs: UserPreferencesRepository) : ViewModel() {
    val state: StateFlow<LoginState> = combine(
        prefs.userName, prefs.userPhone, prefs.avatar, prefs.birthYear, prefs.gender
    ) { n, p, a, b, g -> LoginState(n, p, a, b, g) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LoginState())

    fun save(name: String, phone: String, avatar: String, birthYear: String, gender: String, onDone: () -> Unit) {
        viewModelScope.launch {
            prefs.setUserName(name)
            prefs.setUserPhone(phone)
            prefs.setAvatar(avatar)
            prefs.setBirthYear(birthYear)
            prefs.setGender(gender)
            onDone()
        }
    }
}