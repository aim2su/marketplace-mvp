package com.rebuket.mvp.ui.eco

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rebuket.mvp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoPublishScreen(viewModel: EcoViewModel = hiltViewModel(), onBack: () -> Unit) {
    var titleRu by remember { mutableStateOf("") }
    var titleTj by remember { mutableStateOf("") }
    var titleEn by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.eco_publish_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = titleRu, onValueChange = { titleRu = it },
                label = { Text(stringResource(R.string.eco_name_ru)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = titleTj, onValueChange = { titleTj = it },
                label = { Text(stringResource(R.string.eco_name_tj)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = titleEn, onValueChange = { titleEn = it },
                label = { Text(stringResource(R.string.eco_name_en)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = price, onValueChange = { price = it },
                label = { Text(stringResource(R.string.eco_price)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = contact, onValueChange = { contact = it },
                label = { Text(stringResource(R.string.eco_contact)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    if (titleRu.isNotBlank() && price.toDoubleOrNull() != null) {
                        viewModel.addBouquet(titleRu, titleTj.ifBlank { titleRu }, titleEn.ifBlank { titleRu },
                            price.toDouble(), contact)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.eco_publish_button)) }
        }
    }
}