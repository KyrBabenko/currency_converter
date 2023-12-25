package com.developers.currency_exchange.util

fun String.removeTrailingZeroes(): String {
    val trimmedValue = this.replace("[0]*$".toRegex(), "").replace("\\.$".toRegex(), "")
    return if (trimmedValue.isEmpty()) "0" else trimmedValue
}
