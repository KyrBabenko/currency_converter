package com.developers.currency_exchange.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.developers.currency_exchange.R
import com.developers.currency_exchange.presentation.model.CurrencyUiItem

@Composable
fun Balances(
    modifier: Modifier = Modifier,
    currencies: List<CurrencyUiItem> = emptyList()
) {
    val scrollState = rememberScrollState()

    if (currencies.isEmpty()) {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.no_balances_available),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
        )
    } else {
        Column(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.my_balances).uppercase(),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .horizontalScroll(scrollState)
            ) {
                repeat(currencies.size) {
                    val currencyItem = currencies[it]

                    BalanceItem(
                        modifier = Modifier
                            .padding(end = 20.dp),
                        value = currencyItem.value,
                        currency = currencyItem.name
                    )
                }
            }
        }
    }
}

@Composable
fun BalanceItem(
    modifier: Modifier = Modifier,
    value: Float,
    currency: String
) {
    Text(
        modifier = modifier
            .padding(vertical = 10.dp),
        text = "$value $currency",
        style = MaterialTheme.typography.bodyLarge
    )
}