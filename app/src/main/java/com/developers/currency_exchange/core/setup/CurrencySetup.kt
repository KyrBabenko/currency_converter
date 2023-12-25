package com.developers.currency_exchange.core.setup

import javax.inject.Inject

interface CurrencySetup {

    fun getPollingDelay(): Long

    class Base @Inject constructor(): CurrencySetup {

        override fun getPollingDelay(): Long = 5000
    }
}