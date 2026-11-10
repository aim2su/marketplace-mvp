package com.rebuket.mvp.core.locale

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

val LocalAppContext = staticCompositionLocalOf<Context> { error("LocalAppContext not provided") }
val LocalAppLocale = staticCompositionLocalOf<String> { "ru" }

object LocaleManager {
    const val RU = "ru"
    const val TJ = "tj"
    const val EN = "en"

    fun currentTag(context: Context): String {
        return context.getSharedPreferences("rebuket_prefs", Context.MODE_PRIVATE)
            .getString("app_locale", null) ?: RU
    }

    fun setLocale(context: Context, tag: String) {
        context.getSharedPreferences("rebuket_prefs", Context.MODE_PRIVATE)
            .edit().putString("app_locale", tag).commit()
        (context as? Activity)?.recreate()
    }
}