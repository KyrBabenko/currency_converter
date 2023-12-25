package com.developers.currency_exchange.domain.repository

import java.math.BigDecimal

interface BalanceChanger {

    suspend fun doTransaction(
        sell: String,
        sellAmount: BigDecimal,
        receive: String,
        receiveAmount: BigDecimal
    )
}