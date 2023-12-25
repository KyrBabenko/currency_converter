package com.developers.currency_exchange.domain.use_case

import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.max

class InputValidatorUseCase @Inject constructor() {

    fun invoke(amount: BigDecimal): Boolean {
        val decimalsNumber = max(0, amount.stripTrailingZeros().scale())
        return decimalsNumber <= AVAILABLE_DECIMALS_NUMBER
    }

    companion object {
        private const val AVAILABLE_DECIMALS_NUMBER = 2
    }
}