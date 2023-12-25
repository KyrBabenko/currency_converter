package com.developers.currency_exchange.domain.use_case

import com.developers.currency_exchange.domain.model.CurrencyRate
import com.developers.currency_exchange.domain.repository.RatesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyRateUseCase @Inject constructor(
    private val ratesRepository: RatesRepository,
) {

    data class CurrencyRateError(val exception: Throwable? = null): Exception(exception)

    /**
     * @throws [CurrencyRateError]
     */
    suspend fun invoke(): Flow<CurrencyRate> {
        return ratesRepository.getRate()
    }
}