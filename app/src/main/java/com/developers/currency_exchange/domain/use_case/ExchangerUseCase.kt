package com.developers.currency_exchange.domain.use_case

import com.developers.currency_exchange.domain.model.Rate
import com.developers.currency_exchange.domain.repository.BalanceChanger
import com.developers.currency_exchange.domain.repository.BalanceVerifier
import com.developers.currency_exchange.domain.repository.CommissionApplier
import com.developers.currency_exchange.domain.repository.CommissionCounter
import com.developers.currency_exchange.presentation.home.model.SuccessExchange
import com.developers.currency_exchange.util.removeTrailingZeroes
import java.math.BigDecimal
import javax.inject.Inject

class ExchangerUseCase @Inject constructor(
    private val commissionCounter: CommissionCounter,
    private val commissionApplier: CommissionApplier,
    private val balanceVerifier: BalanceVerifier,
    private val balanceChanger: BalanceChanger,
    private val converterUseCase: ConverterUseCase,
) {

    data class NotEnoughMoneyException(override val message: String? = null) : Exception(message)
    data class SameItemsException(override val message: String? = null): Exception(message)

    /**
     * @throws [NotEnoughMoneyException]
     */
    suspend fun exchangeMoney(
        sell: Rate,
        receive: Rate,
        amount: BigDecimal,
        base: String,
    ): SuccessExchange {
        if (sell.name == receive.name) {
            throw SameItemsException()
        }

        val commission: BigDecimal = commissionCounter.getCommission(sell.name, amount)
        val sellAmount = amount.add(commission)
        val isTransactionPermit = balanceVerifier.isEnoughBalance(sell.name, sellAmount)

        if (isTransactionPermit) {
            val receiveAmount = converterUseCase.convert(
                sell = sell,
                receive = receive,
                amount = amount,
                base = base
            )
            balanceChanger.doTransaction(
                sell = sell.name,
                sellAmount = sellAmount,
                receive = receive.name,
                receiveAmount = receiveAmount
            )
            commissionApplier.transactionCompleted(sell.name)

            return SuccessExchange(
                sellAmount = amount.toPlainString(),
                sellCurrency = sell.name,
                receiveAmount = receiveAmount.toPlainString().removeTrailingZeroes(),
                receiveCurrency = receive.name,
                commissionAmount = commission.toPlainString().removeTrailingZeroes(),
                commissionCurrency = sell.name,
            )
        } else {
            throw NotEnoughMoneyException()
        }
    }
}