package com.developers.currency_exchange.presentation.home.mvi

import com.developers.currency_exchange.presentation.home.model.BalanceUi

data class HomeState(
    val balances: List<BalanceUi> = emptyList(),
    val cellCurrencies: List<String> = emptyList(),
    val receiveCurrencies: List<String> = emptyList(),
    val selectedSellCurrency: String = "",
    val selectedReceiveCurrency: String = "",
    val cellAmount: String = "",
    val receiveAmount: String = "",
)
