package com.developers.currency_exchange.domain.use_case

import javax.inject.Inject

class InputValidatorUseCase @Inject constructor() {

    fun invoke(amount: String): Boolean {
        val dotCount = amount.count { char -> char == DOT_DELIMITER }
        if (dotCount <= AVAILABLE_DELIMITER_NUMBER) {
            return true
        }
        return false
    }

    companion object {
        private const val AVAILABLE_DELIMITER_NUMBER = 1
        private const val DOT_DELIMITER = '.'
    }
}