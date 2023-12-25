package com.developers.currency_exchange.domain.repository

import java.math.BigDecimal

interface BalanceController {

    suspend fun addBalances(names: Map<String, BigDecimal>)
}