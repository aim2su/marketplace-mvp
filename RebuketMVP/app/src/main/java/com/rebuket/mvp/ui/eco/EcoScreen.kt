package com.rebuket.mvp.ui.eco

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocalAppLocale
import com.rebuket.mvp.core.theme.LocalExtendedPalette

@Composable
fun EcoScreen(viewModel: EcoViewModel = hiltViewModel(), onPublishClick: () -> Unit) {
    val C = LocalExtendedPalette.current
    val locale = LocalAppLocale.current
    val bouquets by viewModel.bouquets.collectAsStateWithLifecycle()
    var originalPrice by remember { mutableStateOf("500") }

    val suggestedPrice = (originalPrice.toIntOrNull() ?: 0) / 2

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 100.dp)) {
        Box(modifier = Modifier.padding(16.dp).fillMaxWidth().height(180.dp).clip(RoundedCornerShape(16.dp))) {
            Image(
                painter = painterResource(R.drawable.banner_eco),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(Modifier.matchParentSize().background(Brush.linearGradient(listOf(C.primary.copy(alpha = 0.13f), C.primaryDark.copy(alpha = 0.8f)))))
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(stringResource(R.string.eco_banner_title), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(stringResource(R.string.eco_banner_desc), color = Color.White.copy(alpha = 0.95f), fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }

        Card(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = C.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Filled.Bolt, contentDescription = null, tint = C.primary, modifier = Modifier.size(16.dp))
                    Text(stringResource(R.string.eco_calc_title), color = C.text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Text(stringResource(R.string.eco_calc_question), color = C.muted, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                OutlinedTextField(
                    value = originalPrice,
                    onValueChange = { originalPrice = it.filter { ch -> ch.isDigit() }.take(6) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    suffix = { Text("TJS", color = C.muted, fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(10.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp).clip(RoundedCornerShape(10.dp)).background(C.primaryLight).padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(stringResource(R.string.eco_calc_sell_for), color = C.muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("$suggestedPrice TJS", color = C.primary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(stringResource(R.string.eco_calc_return), color = C.muted, fontSize = 11.sp)
                        Text("50%", color = C.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Text(stringResource(R.string.eco_how_it_works), color = C.text, fontWeight = FontWeight.Bold, fontSize = 17.sp, modifier = Modifier.padding(16.dp))
        listOf(
            Triple(Icons.Filled.Eco, R.string.eco_step_1_title, R.string.eco_step_1_desc),
            Triple(Icons.Filled.LocalOffer, R.string.eco_step_2_title, R.string.eco_step_2_desc),
            Triple(Icons.Filled.ChatBubbleOutline, R.string.eco_step_3_title, R.string.eco_step_3_desc)
        ).forEach { (icon, titleRes, descRes) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(C.primaryLight), contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = C.primary, modifier = Modifier.size(18.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(titleRes), color = C.text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(stringResource(descRes), color = C.muted, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                }
            }
        }

        Text(stringResource(R.string.eco_examples), color = C.text, fontWeight = FontWeight.Bold, fontSize = 17.sp, modifier = Modifier.padding(16.dp))

        if (bouquets.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.eco_empty), color = C.muted)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().height(((bouquets.size + 1) / 2 * 220).dp).padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                userScrollEnabled = false
            ) {
                items(bouquets, key = { it.id }) { b ->
                    val title = when (locale) {
                        "tj" -> b.titleTj
                        "en" -> b.titleEn
                        else -> b.titleRu
                    }
                    Card(colors = CardDefaults.cardColors(containerColor = C.surface), shape = RoundedCornerShape(14.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(title, color = C.text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${b.price.toInt()} TJS", color = C.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(top = 6.dp))
                            Text(b.contact, color = C.muted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Button(
                onClick = onPublishClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = C.primary)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text("  " + stringResource(R.string.eco_publish_btn), fontWeight = FontWeight.Bold)
            }
        }
    }
}