package com.rebuket.mvp.ui.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rebuket.mvp.R
import com.rebuket.mvp.core.locale.LocalAppLocale
import com.rebuket.mvp.core.theme.LocalExtendedPalette
import com.rebuket.mvp.ui.components.getProductImages

private val CATEGORIES = listOf("Все", "Розы", "Пионы", "Тюльпаны", "Гортензии", "Композиции")
private val POPULAR = listOf("Розы", "Пионы", "Тюльпаны", "Эко")

@Composable
fun SearchOverlay(
    onClose: () -> Unit,
    onOpenProduct: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val C = LocalExtendedPalette.current
    val locale = LocalAppLocale.current
    val query by viewModel.query.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()
    val results by viewModel.results.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { viewModel.reset(); focusRequester.requestFocus() }

    Column(modifier = Modifier.fillMaxSize().background(C.bg)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(C.surface).padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onClose, modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(C.bg)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = C.text)
            }
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.setQuery(it) },
                placeholder = { Text(stringResource(R.string.search_hint_short), fontSize = 14.sp) },
                modifier = Modifier.weight(1f).focusRequester(focusRequester),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setQuery("") }) {
                            Icon(Icons.Filled.Close, contentDescription = null, tint = C.muted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth().background(C.surface).padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(CATEGORIES) { cat ->
                val active = cat == category
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(50))
                        .background(if (active) C.primary else C.bg)
                        .clickable { viewModel.setCategory(cat) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(cat, color = if (active) Color.White else C.text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (query.isBlank() && category == "Все") {
                item {
                    Text(
                        stringResource(R.string.search_popular).uppercase(),
                        color = C.muted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        POPULAR.forEach { s ->
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(50)).background(C.surface)
                                    .clickable { viewModel.setQuery(s) }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Text(s, color = C.text, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    if (results.isNotEmpty()) "${stringResource(R.string.search_found)}: ${results.size}" else stringResource(R.string.search_results),
                    color = C.muted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }
            if (results.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.Search, contentDescription = null, tint = C.muted, modifier = Modifier.size(40.dp))
                        Text(stringResource(R.string.search_nothing), color = C.text, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp))
                        Text(stringResource(R.string.search_try_other), color = C.muted, fontSize = 12.sp)
                    }
                }
            } else {
                items(results, key = { it.id }) { p ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(C.surface)
                            .clickable { onOpenProduct(p.id); onClose() }.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = getProductImages(p.id).first()),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(10.dp))
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.getLocalizedName(locale), color = C.text, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                            Text("${p.price.toInt()} TJS", color = C.primary, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                }
            }
        }
    }
}