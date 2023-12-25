package com.developers.currency_exchange.core

import timber.log.Timber
import javax.inject.Inject

interface Logger {

    fun d(message: String)

    fun e(message: String)

    class Base @Inject constructor(): Logger {

        override fun d(message: String) {
            Timber.i(message)
        }

        override fun e(message: String) {
            Timber.e(message)
        }
    }
}