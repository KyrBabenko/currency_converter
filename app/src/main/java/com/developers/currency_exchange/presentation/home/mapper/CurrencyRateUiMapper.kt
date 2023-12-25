package com.developers.currency_exchange.presentation.home.mapper

import com.developers.currency_exchange.domain.model.CurrencyRate
import com.developers.currency_exchange.presentation.home.model.BalanceUi
import java.math.BigDecimal

fun CurrencyRate.mapToBalances(): Map<String, BigDecimal> {
    return rates.associate {
        it.name to BigDecimal.ZERO
    }
}

fun Map<String, BigDecimal>.mapToBalancesUi(): List<BalanceUi> {
    return map {
        BalanceUi(name = it.key, amount = it.value.toFloat())
    }
}