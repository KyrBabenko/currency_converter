package com.developers.currency_exchange.data

import com.developers.currency_exchange.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApi
) : CurrencyRepository {

    /**
     * @throws
     */
    override suspend fun getRate(): Map<String, Float> {
        return api.getRate().body()!!.rates // TODO: HANDLE it
    }
}