package com.rebuket.mvp.core.toast

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ToastController(private val scope: CoroutineScope) {
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message
    private var job: Job? = null

    fun show(text: String) {
        job?.cancel()
        _message.value = text
        job = scope.launch {
            delay(2400)
            if (_message.value == text) _message.value = null
        }
    }

    fun clear() {
        job?.cancel()
        _message.value = null
    }
}

val LocalToast = staticCompositionLocalOf<ToastController> {
    error("ToastController not provided")
}