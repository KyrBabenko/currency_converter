package com.developers.currency_exchange.presentation.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.developers.currency_exchange.R
import com.developers.currency_exchange.presentation.model.AmountStatus
import com.developers.currency_exchange.presentation.model.CurrencyUiItem
import com.developers.currency_exchange.ui.components.Balances
import com.developers.currency_exchange.ui.components.CurrencyExchange
import com.developers.currency_exchange.ui.components.CurrencyExchangeToolbar
import com.developers.currency_exchange.ui.components.SubmitButton

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = focusManager::clearFocus
                )
        ) {
            CurrencyExchangeToolbar()
            Balances(
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp),
                currencies = listOf(
                    CurrencyUiItem(1000f, "USD"),
                    CurrencyUiItem(0f, "EUR"),
                    CurrencyUiItem(0f, "EUR"),
                    CurrencyUiItem(0f, "EUR"),
                    CurrencyUiItem(0f, "EUR"),
                    CurrencyUiItem(0f, "EUR"),
                    CurrencyUiItem(0f, "EUR"),
                    CurrencyUiItem(0f, "EUR"),
                    CurrencyUiItem(0f, "EUR"),
                    CurrencyUiItem(0f, "EUR"),
                )
            )
            CurrencyExchange(
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                // String
                cellAmount = "123",
                // (selectedCurrency: String) -> Unit
                onChangeCellAmount = {},
                // List<String>
                cellCurrencies = listOf("EUR", "USD", "GBP"),
                // String
                cellSelectedCurrency = "EUR",
                // (selectedCurrency: String) -> Unit
                onChangeCellCurrency = {},
                // String
                receiveAmount = "10",
                // AmountStatus
                receiveAmountStatus =  AmountStatus.POSITIVE,
                // List<String>
                receiveCurrencies = listOf("USD", "EUR", "GBP"),
                // String
                selectedReceiveCurrency = "USD",
                // (selectedCurrency: String) -> Unit
                onChangeReceiveCurrency = {},
                onKeyboardAction = {
                    showSuccess(context)
                }
            )
        }
        SubmitButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, bottom = 15.dp),
            text = stringResource(id = R.string.submit).uppercase(),
            enabled = true,
            onClick = {
                showSuccess(context)
            }
        )
    }
}

fun showSuccess(context: Context) {
    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
}
