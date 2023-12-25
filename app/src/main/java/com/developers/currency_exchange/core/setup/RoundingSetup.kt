package com.developers.currency_exchange.core.setup

import java.math.RoundingMode
import javax.inject.Inject

interface RoundingSetup {

    fun getRoundingCountSign(): Int

    fun getRoundingMode(): RoundingMode

    fun getRoundingUiSign(): Int

    class Base @Inject constructor(): RoundingSetup {

        override fun getRoundingCountSign(): Int = DEFAULT_ROUNDING_COUNT_SIGN

        override fun getRoundingMode(): RoundingMode = DEFAULT_ROUNDING_MODE

        override fun getRoundingUiSign(): Int = DEFAULT_ROUNDING_UI_SIGN

        companion object {
            private const val DEFAULT_ROUNDING_COUNT_SIGN = 10
            private const val DEFAULT_ROUNDING_UI_SIGN = 2
            private val DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP
        }
    }
}