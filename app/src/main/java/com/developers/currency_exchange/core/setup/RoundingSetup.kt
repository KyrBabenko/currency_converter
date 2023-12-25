package com.developers.currency_exchange.core.setup

import java.math.RoundingMode
import javax.inject.Inject

interface RoundingSetup {

    fun getRoundingSign(): Int

    fun getRoundingMode(): RoundingMode

    class Base @Inject constructor(): RoundingSetup {

        override fun getRoundingSign(): Int = DEFAULT_ROUNDING_SIGN

        override fun getRoundingMode(): RoundingMode = DEFAULT_ROUNDING_MODE

        companion object {
            private const val DEFAULT_ROUNDING_SIGN = 10
            private val DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP
        }
    }
}