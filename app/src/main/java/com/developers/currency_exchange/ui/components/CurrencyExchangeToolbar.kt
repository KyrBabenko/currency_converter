package com.developers.currency_exchange.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.developers.currency_exchange.R

@Composable
fun CurrencyExchangeToolbar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.inversePrimary,
            text = stringResource(R.string.currency_converter)
        )
    }
}