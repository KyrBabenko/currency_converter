package com.developers.currency_exchange.domain.use_case

import com.developers.currency_exchange.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRateUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {

    suspend fun invoke(): Map<String, Float> {
        return currencyRepository.getRate()
    }
}