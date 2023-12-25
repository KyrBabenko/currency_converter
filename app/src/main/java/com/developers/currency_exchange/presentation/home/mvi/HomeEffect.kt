package com.developers.currency_exchange.presentation.home.mvi

sealed class HomeEffect {
    object NoCurrencyRateUpdates: HomeEffect()
}
