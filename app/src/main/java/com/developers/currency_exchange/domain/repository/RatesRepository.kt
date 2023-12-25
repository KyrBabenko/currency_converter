package com.developers.currency_exchange.domain.repository

import com.developers.currency_exchange.domain.model.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface RatesRepository {

    suspend fun getRate(): Flow<CurrencyRate>
}