package com.developers.currency_exchange.data.repository

import com.developers.currency_exchange.core.setup.CurrencySetup
import com.developers.currency_exchange.data.api.CurrencyApi
import com.developers.currency_exchange.data.mapper.mapToDomain
import com.developers.currency_exchange.domain.model.CurrencyRate
import com.developers.currency_exchange.domain.repository.RatesRepository
import com.developers.currency_exchange.domain.use_case.CurrencyRateUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class RatesRepositoryInMemory @Inject constructor(
    private val api: CurrencyApi,
    private val currencySetup: CurrencySetup,
) : RatesRepository {

    /**
     * Short polling
     */
    override suspend fun getRate(): Flow<CurrencyRate> {
        return flow {
            while (coroutineContext.isActive) {
                val response = api.getRate()
                val rateDTO = response.body()
                if (response.isSuccessful && rateDTO != null) {
                    emit(rateDTO.mapToDomain())
                } else {
                    throw CurrencyRateUseCase.CurrencyRateError()
                }
                delay(currencySetup.getPollingDelay())
            }
        }
    }
}