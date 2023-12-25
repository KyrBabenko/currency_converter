package com.developers.currency_exchange.domain.repository

import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface BalanceObserver {

    fun subscribe(): Flow<Map<String, BigDecimal>>
}