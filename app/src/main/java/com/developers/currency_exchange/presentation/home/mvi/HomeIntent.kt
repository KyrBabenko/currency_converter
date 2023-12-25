package com.developers.currency_exchange.presentation.home.mvi

interface HomeIntent {

    fun observeCurrencyRateChanges()

    fun cancelJob()
}
