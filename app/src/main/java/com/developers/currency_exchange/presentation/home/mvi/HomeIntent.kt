package com.developers.currency_exchange.presentation.home.mvi

import com.developers.currency_exchange.domain.model.Rate

interface HomeIntent {

    fun observeCurrencyRateChanges()
    fun cancelJob()
    fun exchangeMoney()
    fun onChangeSellAmount(sellAmount: String)
    fun onChangeSellCurrency(sell: Rate)
    fun onChangeReceiveCurrency(receive: Rate)
}
