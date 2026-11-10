package com.rebuket.mvp.ui.constructor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.theme.ExtendedPalette
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.core.toast.LocalToast

@Composable
fun ConstructorScreen(
    viewModel: ConstructorViewModel = hiltViewModel(),
    onSuccess: () -> Unit
) {
    val C = LocalExtendedPalette.current
    val toast = LocalToast.current

    val type by viewModel.type.collectAsStateWithLifecycle()
    val flowers by viewModel.flowers.collectAsStateWithLifecycle()
    val packaging by viewModel.packaging.collectAsStateWithLifecycle()
    val ribbon by viewModel.ribbon.collectAsStateWithLifecycle()
    val card by viewModel.card.collectAsStateWithLifecycle()
    val totalFlowers by viewModel.totalFlowers.collectAsStateWithLifecycle()
    val totalPrice by viewModel.totalPrice.collectAsStateWithLifecycle()

    val packName = stringResource(PACKAGING_OPTIONS.find { it.key == packaging }?.nameRes ?: R.string.packaging_craft)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 100.dp)
        ) {
            Box(
                modifier = Modifier.padding(16.dp).fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)).background(C.primaryLight)
            ) {
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                    Image(
                        painter = painterResource(R.drawable.constructor_template),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))))
                    )
                    Row(
                        modifier = Modifier.align(Alignment.TopStart).padding(12.dp)
                            .clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.92f))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(Icons.Filled.LocalFlorist, contentDescription = null, tint = C.primary, modifier = Modifier.size(11.dp))
                        Text(stringResource(R.string.constructor_template).uppercase(), color = C.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                        Text(stringResource(R.string.constructor_your_base).uppercase(), color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(
                            if (totalFlowers > 0) "Букет · $totalFlowers цветов" else stringResource(R.string.choose_flowers_below),
                            color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ConstructorTypeChip("bouquet", R.string.constructor_type_bouquet, Icons.Filled.LocalFlorist, type, C) { viewModel.setType(it) }
                ConstructorTypeChip("cake", R.string.constructor_type_cake, Icons.Filled.Cake, type, C) { viewModel.setType(it) }
                ConstructorTypeChip("sweet", R.string.constructor_type_sweet, Icons.Filled.CardGiftcard, type, C) { viewModel.setType(it) }
            }

            StepHeader(C, 1, stringResource(R.string.constructor_step_flowers), stringResource(R.string.constructor_step_flowers_hint))
            Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FLOWER_TYPES.forEach { ft ->
                    val count = flowers[ft.key] ?: 0
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(C.surface)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stringResource(ft.nameRes), color = C.text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(stringResource(R.string.per_item, ft.price), color = C.muted, fontSize = 11.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                                    .background(if (count == 0) C.bg else C.surface)
                                    .clickable(enabled = count > 0) { viewModel.changeFlower(ft.key, -1) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Remove, contentDescription = null, tint = if (count == 0) C.muted else C.text, modifier = Modifier.size(16.dp))
                            }
                            Text("$count", color = C.text, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.width(24.dp))
                            Box(
                                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(C.primary)
                                    .clickable { viewModel.changeFlower(ft.key, 1) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            StepHeader(C, 2, stringResource(R.string.constructor_step_packaging), "")
            Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PACKAGING_OPTIONS.forEach { po ->
                    OptionChip(stringResource(po.nameRes), po.price, packaging == po.key, C) { viewModel.setPackaging(po.key) }
                }
            }

            StepHeader(C, 3, stringResource(R.string.constructor_step_ribbon), "")
            Row(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RIBBON_OPTIONS.forEach { ro ->
                    OptionChip(stringResource(ro.nameRes), ro.price, ribbon == ro.key, C) { viewModel.setRibbon(ro.key) }
                }
            }

            StepHeader(C, 4, stringResource(R.string.constructor_step_card), stringResource(R.string.constructor_step_card_hint))
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = card,
                    onValueChange = { viewModel.setCard(it) },
                    placeholder = { Text(stringResource(R.string.card_hint), fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(14.dp)
                )
                Text(
                    stringResource(R.string.card_counter, card.length),
                    color = C.muted, fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            color = C.surface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.constructor_total).uppercase(), color = C.muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("$totalPrice TJS", color = C.primary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("$totalFlowers · $packName", color = C.muted, fontSize = 10.sp)
                }
                Button(
                    onClick = {
                        viewModel.submitOrder(onSuccess)
                        toast.show("Заявка отправлена!")
                    },
                    enabled = totalFlowers > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = C.primary)
                ) {
                    Text(stringResource(R.string.constructor_send), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RowScope.ConstructorTypeChip(
    key: String,
    labelRes: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    current: String,
    C: ExtendedPalette,
    onClick: (String) -> Unit
) {
    val active = key == current
    Column(
        modifier = Modifier.weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(if (active) C.primary else C.surface)
            .clickable { onClick(key) }
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(icon, contentDescription = null, tint = if (active) Color.White else C.primary, modifier = Modifier.size(22.dp))
        Text(stringResource(labelRes), color = if (active) Color.White else C.text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun OptionChip(label: String, price: Int, active: Boolean, C: ExtendedPalette, onClick: () -> Unit) {
    Row(
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
            .background(if (active) C.primary else C.surface)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(label, color = if (active) Color.White else C.text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        if (price > 0) Text("$price", color = if (active) Color.White.copy(alpha = 0.8f) else C.muted, fontSize = 10.sp)
        if (active) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
    }
}

@Composable
private fun StepHeader(C: ExtendedPalette, num: Int, title: String, hint: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier.size(26.dp).clip(CircleShape).background(C.primary),
            contentAlignment = Alignment.Center
        ) {
            Text("$num", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = C.text, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            if (hint.isNotEmpty()) Text(hint, color = C.muted, fontSize = 11.sp)
        }
    }
}