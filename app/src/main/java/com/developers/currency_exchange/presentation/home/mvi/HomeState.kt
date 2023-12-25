package com.developers.currency_exchange.presentation.home.mvi

import com.developers.currency_exchange.domain.model.CurrencyRate
import com.developers.currency_exchange.domain.model.Rate
import com.developers.currency_exchange.presentation.home.model.BalanceUi
import com.developers.currency_exchange.presentation.model.AmountStatus

data class HomeState(
    val balances: List<BalanceUi> = emptyList(),
    val currencyRate: CurrencyRate = CurrencyRate(),
    val sellCurrencies: List<Rate> = emptyList(),
    val receiveCurrencies: List<Rate> = emptyList(),
    val sellSelectedCurrency: Rate = Rate(),
    val receiveSelectedCurrency: Rate = Rate(),
    val sellAmount: String = "0,0",
    val receiveAmount: String = "0,0",
    val receiverAmountStatus: AmountStatus = AmountStatus.NEUTRAL,
)
