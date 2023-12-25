package com.developers.currency_exchange.domain.repository

import java.math.BigDecimal

interface CommissionCounter {

    suspend fun getCommission(currency: String, amount: BigDecimal): BigDecimal
}