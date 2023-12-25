package com.developers.currency_exchange.data.repository

import com.developers.currency_exchange.core.setup.CommissionSetup
import com.developers.currency_exchange.core.setup.RoundingSetup
import com.developers.currency_exchange.domain.repository.CommissionApplier
import com.developers.currency_exchange.domain.repository.CommissionCounter
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Commission @Inject constructor(
    private val commissionSetup: CommissionSetup,
    private val roundingSetup: RoundingSetup,
) : CommissionApplier, CommissionCounter {

    private val transactionsAmount = ConcurrentHashMap<String, Int>()
    private val mutex = Mutex()

    /**
     * Must be called after transaction completion to properly count discount
     */
    override suspend fun transactionCompleted(currency: String) {
        mutex.withLock {
            transactionsAmount[currency] = transactionsAmount.getOrDefault(
                currency,
                DEFAULT_TRANSACTION_AMOUNT
            ).inc()
        }
    }

    override suspend fun getCommission(currency: String, amount: BigDecimal): BigDecimal {
        mutex.withLock {
            val transactionsCompleted = transactionsAmount.getOrDefault(
                currency,
                DEFAULT_EMPTY_TRANSACTIONS_AMOUNT
            )
            val freeTransactionsAmount = commissionSetup.getFreeTransactionsAmount()

            if (transactionsCompleted < freeTransactionsAmount) {
                return COMMISSION_FOR_FREE_TRANSACTIONS.toBigDecimal()
            } else {
                val commissionPercentage = commissionSetup.getCommissionPercentage()
                return getCommission(amount, commissionPercentage)
            }
        }
    }

    private fun getCommission(amount: BigDecimal, commissionPercentage: Int): BigDecimal {
        return amount
            .multiply(commissionPercentage.toBigDecimal())
            .divide(
                BigDecimal.valueOf(HUNDRED),
                roundingSetup.getRoundingSign(),
                roundingSetup.getRoundingMode()
            )
    }

    companion object {
        private const val DEFAULT_EMPTY_TRANSACTIONS_AMOUNT = 0
        private const val DEFAULT_TRANSACTION_AMOUNT = 1
        private const val COMMISSION_FOR_FREE_TRANSACTIONS = 0
        private const val HUNDRED = 100L
    }
}