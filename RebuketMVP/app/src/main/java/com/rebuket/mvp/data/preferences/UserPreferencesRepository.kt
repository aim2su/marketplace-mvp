package com.rebuket.mvp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val PALETTE_KEY = stringPreferencesKey("palette_key")
    private val THEME_KEY = stringPreferencesKey("theme_mode")
    private val NAME_KEY = stringPreferencesKey("user_name")
    private val PHONE_KEY = stringPreferencesKey("user_phone")
    private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications_enabled")
    private val FONT_SIZE_KEY = stringPreferencesKey("font_size")
    private val CITY_KEY = stringPreferencesKey("city")
    private val AVATAR_KEY = stringPreferencesKey("user_avatar")
    private val BIRTH_YEAR_KEY = stringPreferencesKey("user_birth_year")
    private val GENDER_KEY = stringPreferencesKey("user_gender")

    val themeMode: Flow<String> = context.dataStore.data.map { it[THEME_KEY] ?: "SYSTEM" }
    val paletteKey: Flow<String> = context.dataStore.data.map { it[PALETTE_KEY] ?: "classic" }
    val userName: Flow<String> = context.dataStore.data.map { it[NAME_KEY] ?: "" }
    val userPhone: Flow<String> = context.dataStore.data.map { it[PHONE_KEY] ?: "" }
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[NOTIFICATIONS_KEY] ?: true }
    val fontSize: Flow<String> = context.dataStore.data.map { it[FONT_SIZE_KEY] ?: "m" }
    val city: Flow<String> = context.dataStore.data.map { it[CITY_KEY] ?: "Душанбе" }
    val avatar: Flow<String> = context.dataStore.data.map { it[AVATAR_KEY] ?: "" }
    val birthYear: Flow<String> = context.dataStore.data.map { it[BIRTH_YEAR_KEY] ?: "" }
    val gender: Flow<String> = context.dataStore.data.map { it[GENDER_KEY] ?: "" }

    suspend fun setThemeMode(mode: String) { context.dataStore.edit { it[THEME_KEY] = mode } }
    suspend fun setPaletteKey(key: String) { context.dataStore.edit { it[PALETTE_KEY] = key } }
    suspend fun setUserName(name: String) { context.dataStore.edit { it[NAME_KEY] = name } }
    suspend fun setUserPhone(phone: String) { context.dataStore.edit { it[PHONE_KEY] = phone } }
    suspend fun setNotificationsEnabled(enabled: Boolean) { context.dataStore.edit { it[NOTIFICATIONS_KEY] = enabled } }
    suspend fun setFontSize(size: String) { context.dataStore.edit { it[FONT_SIZE_KEY] = size } }
    suspend fun setCity(city: String) { context.dataStore.edit { it[CITY_KEY] = city } }
    suspend fun setAvatar(data: String) { context.dataStore.edit { it[AVATAR_KEY] = data } }
    suspend fun setBirthYear(year: String) { context.dataStore.edit { it[BIRTH_YEAR_KEY] = year } }
    suspend fun setGender(g: String) { context.dataStore.edit { it[GENDER_KEY] = g } }
    suspend fun clearProfile() {
        context.dataStore.edit {
            it.remove(NAME_KEY)
            it.remove(PHONE_KEY)
            it.remove(AVATAR_KEY)
            it.remove(BIRTH_YEAR_KEY)
            it.remove(GENDER_KEY)
        }
    }
}