package com.rebuket.mvp.ui.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rebuket.mvp.R
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class ChatMessage(val id: String, val fromUser: Boolean, val text: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(onBack: () -> Unit = {}) {
    val C = LocalExtendedPalette.current
    val welcome = stringResource(R.string.feedback_welcome)
    val autoReply = stringResource(R.string.feedback_auto_reply)
    val messages = remember { mutableStateListOf(ChatMessage("w", false, welcome)) }
    var input by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    fun sendMessage() {
        if (input.isBlank()) return
        val text = input.trim()
        messages.add(ChatMessage("u${System.currentTimeMillis()}", true, text))
        input = ""
        scope.launch {
            delay(1500)
            messages.add(ChatMessage("r${System.currentTimeMillis()}", false, autoReply))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.feedback_title), fontWeight = FontWeight.Bold) },
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
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth().weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages, key = { it.id }) { m ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (m.fromUser) Arrangement.End else Arrangement.Start
                    ) {
                        if (!m.fromUser) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(C.primaryLight), contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.Person, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
                            }
                            Spacer(Modifier.padding(horizontal = 4.dp))
                        }
                        Box(
                            modifier = Modifier.widthIn(max = 260.dp).clip(RoundedCornerShape(16.dp))
                                .background(if (m.fromUser) C.primary else C.surface)
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(m.text, color = if (m.fromUser) Color.White else C.text, fontSize = 13.sp, lineHeight = 20.sp)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().background(C.surface).padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = { Text(stringResource(R.string.feedback_input_hint), fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { sendMessage() })
                )
                IconButton(
                    onClick = { sendMessage() },
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(if (input.isNotBlank()) C.primary else C.muted)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}