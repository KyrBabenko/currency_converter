package com.developers.currency_exchange.domain.repository

import java.math.BigDecimal

interface BalanceVerifier {

    suspend fun isEnoughBalance(name: String, amount: BigDecimal): Boolean
}