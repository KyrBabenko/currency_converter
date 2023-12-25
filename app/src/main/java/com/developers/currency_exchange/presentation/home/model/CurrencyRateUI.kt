package com.developers.currency_exchange.presentation.home.model

import java.time.LocalDateTime

data class CurrencyRateUI(
    val base: String,
    val date: LocalDateTime,
    val rates: List<RateUi>
)
