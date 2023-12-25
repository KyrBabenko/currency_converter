package com.developers.currency_exchange.domain.repository

interface CommissionApplier {

    suspend fun transactionCompleted(currency: String)
}