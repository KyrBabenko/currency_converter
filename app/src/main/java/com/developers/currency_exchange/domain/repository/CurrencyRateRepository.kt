package com.developers.currency_exchange.domain.repository

interface CurrencyRepository {

    suspend fun getRate(): Map<String, Float>
}