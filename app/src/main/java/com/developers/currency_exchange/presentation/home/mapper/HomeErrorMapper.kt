package com.developers.currency_exchange.presentation.home.mapper

import com.developers.currency_exchange.R
import com.developers.currency_exchange.presentation.home.HomeError

fun HomeError.getResource(): Int {
    return when (this) {
        HomeError.EXCHANGE_ZERO -> R.string.exchange_zero_error
        HomeError.NOT_ENOUGH_MONEY -> R.string.not_enough_money_error
        HomeError.NO_CURRENCY_RATE_UPDATES -> R.string.no_currency_rate_updates_error
        HomeError.SAME_ITEMS -> R.string.same_items_error
    }
}