package com.developers.currency_exchange.presentation.home.mvi

import com.developers.currency_exchange.presentation.home.HomeError
import com.developers.currency_exchange.presentation.home.model.SuccessExchange

sealed class HomeEffect {
    data class ShowError(val error: HomeError) : HomeEffect()
    data class ShowSuccessExchange(val successExchange: SuccessExchange) : HomeEffect()
}
