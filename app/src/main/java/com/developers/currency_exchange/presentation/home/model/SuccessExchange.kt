package com.developers.currency_exchange.presentation.home.model

data class SuccessExchange(
    val sellAmount: String,
    val sellCurrency: String,
    val receiveAmount: String,
    val receiveCurrency: String,
    val commissionAmount: String,
    val commissionCurrency: String,
)
