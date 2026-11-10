package com.rebuket.mvp.ui.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
fun CheckoutScreen(viewModel: CheckoutViewModel = hiltViewModel(), onDone: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.checkout_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.checkout_description), modifier = Modifier.padding(vertical = 16.dp))

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text(stringResource(R.string.name_label)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = phone, onValueChange = { phone = it },
            label = { Text(stringResource(R.string.phone_label)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        Spacer(Modifier.weight(1f))
        Button(onClick = { viewModel.placeOrder(onDone) },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && phone.isNotBlank()) {
            Text(stringResource(R.string.confirm_order))
        }
    }
}