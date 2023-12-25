package com.developers.currency_exchange.domain.model

import java.time.LocalDateTime

data class CurrencyRate(
    val base: String,
    val date: LocalDateTime,
    val rates: List<Rate>
)
