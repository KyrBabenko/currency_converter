package com.developers.currency_exchange.domain.use_case

import com.developers.currency_exchange.core.setup.RoundingSetup
import com.developers.currency_exchange.domain.model.Rate
import com.developers.currency_exchange.domain.repository.BalanceChanger
import com.developers.currency_exchange.domain.repository.BalanceVerifier
import com.developers.currency_exchange.domain.repository.CommissionApplier
import com.developers.currency_exchange.domain.repository.CommissionCounter
import com.developers.currency_exchange.presentation.home.model.SuccessExchange
import java.math.BigDecimal
import javax.inject.Inject

class ExchangerUseCase @Inject constructor(
    private val commissionCounter: CommissionCounter,
    private val commissionApplier: CommissionApplier,
    private val balanceVerifier: BalanceVerifier,
    private val balanceChanger: BalanceChanger,
    private val converterUseCase: ConverterUseCase,
    private val roundingSetup: RoundingSetup,
) {

    data class NotEnoughMoneyException(override val message: String? = null) : Exception(message)

    /**
     * @throws [NotEnoughMoneyException]
     */
    suspend fun exchangeMoney(
        sell: Rate,
        receive: Rate,
        amount: BigDecimal,
        base: String,
    ): SuccessExchange {
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
            commissionApplier.transactionCompleted(receive.name)

            return SuccessExchange(
                sellAmount = amount.setScale(roundingSetup.getRoundingUiSign()).toString(),
                sellCurrency = sell.name,
                receiveAmount = receiveAmount.setScale(roundingSetup.getRoundingUiSign()).toString(),
                receiveCurrency = receive.name,
                commissionAmount = commission.setScale(roundingSetup.getRoundingUiSign()).toString(),
                commissionCurrency = sell.name,
            )
        } else {
            throw NotEnoughMoneyException()
        }
    }
}