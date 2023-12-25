package com.developers.currency_exchange.domain.model

import java.time.LocalDateTime

data class CurrencyRate(
    val base: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
    val rates: List<Rate> = emptyList(),
)
