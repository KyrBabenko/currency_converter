package com.developers.currency_exchange.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.developers.currency_exchange.R
import com.developers.currency_exchange.presentation.home.mvi.HomeIntent
import com.developers.currency_exchange.presentation.home.mvi.HomeState
import com.developers.currency_exchange.ui.components.Balances
import com.developers.currency_exchange.ui.components.CurrencyExchange
import com.developers.currency_exchange.ui.components.CurrencyExchangeToolbar
import com.developers.currency_exchange.ui.components.SubmitButton

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeState,
    intent: HomeIntent
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        val focusManager = LocalFocusManager.current

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
                currencies = uiState.balances
            )
            CurrencyExchange(
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                sellAmount = uiState.sellAmount,
                onChangeSellAmount = {
                    intent.onChangeSellAmount(it)
                },
                sellCurrencies = uiState.sellCurrencies,
                sellSelectedCurrency = uiState.selectedSellRate,
                onChangeSellCurrency = {
                    intent.onChangeSellCurrency(it)
                },
                receiveAmount = uiState.receiveAmount,
                receiveAmountStatus = uiState.receiverAmountStatus,
                receiveCurrencies = uiState.receiveCurrencies,
                selectedReceiveCurrency = uiState.selectedReceiveRate,
                onChangeReceiveCurrency = {
                    intent.onChangeReceiveCurrency(it)
                },
                onKeyboardAction = {
                    intent.exchangeMoney()
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
                intent.exchangeMoney()
            }
        )
    }
}
