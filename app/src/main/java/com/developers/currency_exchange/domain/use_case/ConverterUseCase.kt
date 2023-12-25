package com.developers.currency_exchange.domain.use_case

import com.developers.currency_exchange.core.setup.RoundingSetup
import com.developers.currency_exchange.domain.model.Rate
import java.math.BigDecimal
import javax.inject.Inject

class ConverterUseCase @Inject constructor(
    private val roundingSetup: RoundingSetup
) {

    /**
     * must be verified outside on equality to 0
     */
    fun convert(
        sell: Rate,
        receive: Rate,
        amount: BigDecimal,
        base: String
    ): BigDecimal {
        val fromBase = sell.name == base && receive.name != base
        val toBase = sell.name != base && receive.name == base
        val cross = sell.name != base && receive.name != base

        return if (cross) {
            crossConvert(amount, sell.rate, receive.rate)
        } else if (fromBase) {
            convertFromBase(amount, receive.rate)
        } else if (toBase) {
            convertToBase(amount, sell.rate)
        } else { // sell.name == receive.name
            amount
        }
    }

    /**
     * EUR -> USD
     *  1  -> 1.12
     */
    private fun convertFromBase(amount: BigDecimal, rate: Float): BigDecimal {
        return amount
            .multiply(rate.toBigDecimal())
            .setScale(roundingSetup.getRoundingCountSign())
    }

    /**
     * USD  -> EUR
     * 1.12 -> 1
     */
    private fun convertToBase(amount: BigDecimal, rate: Float): BigDecimal {
        return amount
            .divide(
                rate.toBigDecimal(),
                roundingSetup.getRoundingCountSign(),
                roundingSetup.getRoundingMode()
            )
    }

    /**
     * USD  -> EUR -> UAH
     * 1.12 ->  1  -> 31.01
     */
    private fun crossConvert(
        amount: BigDecimal,
        sellRate: Float,
        receiveRate: Float,
    ): BigDecimal {
        val amountInBase = convertToBase(amount, sellRate)
        return convertFromBase(amountInBase, receiveRate)
    }
}