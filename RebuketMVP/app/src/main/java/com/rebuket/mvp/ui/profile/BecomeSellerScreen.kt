package com.rebuket.mvp.ui.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rebuket.mvp.R
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.core.toast.LocalToast

private val SELLER_CITIES = listOf("Душанбе", "Худжанд", "Куляб", "Бохтар", "Истаравшан", "Пенджикент", "Турсунзаде")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BecomeSellerScreen(onBack: () -> Unit) {
    val C = LocalExtendedPalette.current
    val toast = LocalToast.current
    var shop by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf(SELLER_CITIES.first()) }
    var cityOpen by remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.become_seller), fontWeight = FontWeight.Bold) },
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
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Brush.linearGradient(listOf(C.primary, C.primaryDark))).padding(18.dp)) {
                Column {
                    Text("Подключите свой магазин", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(stringResource(R.string.become_seller_desc), color = Color.White.copy(alpha = 0.92f), fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
            OutlinedTextField(value = shop, onValueChange = { shop = it }, label = { Text(stringResource(R.string.seller_form_shop)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(R.string.seller_form_name)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text(stringResource(R.string.seller_form_phone)) }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))

            Box {
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        .background(C.surface).clickable { cityOpen = true }.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.courier_form_city), color = C.muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(city, color = C.text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = C.muted, modifier = Modifier.size(20.dp))
                }
                DropdownMenu(expanded = cityOpen, onDismissRequest = { cityOpen = false }, modifier = Modifier.width(300.dp)) {
                    SELLER_CITIES.forEach { ct ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(ct, color = if (ct == city) C.primary else C.text, fontWeight = if (ct == city) FontWeight.Bold else FontWeight.Normal)
                                    if (ct == city) Icon(Icons.Filled.Check, contentDescription = null, tint = C.primary, modifier = Modifier.size(16.dp))
                                }
                            },
                            onClick = { city = ct; cityOpen = false }
                        )
                    }
                }
            }

            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text(stringResource(R.string.seller_form_address)) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Button(onClick = { toast.show("Заявка отправлена! Свяжемся в течение суток."); onBack() }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text(stringResource(R.string.submit_application), fontWeight = FontWeight.Bold)
            }
        }
    }
}