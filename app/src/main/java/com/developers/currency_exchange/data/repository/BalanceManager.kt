package com.developers.currency_exchange.data.repository

import com.developers.currency_exchange.core.setup.RoundingSetup
import com.developers.currency_exchange.domain.repository.BalanceChanger
import com.developers.currency_exchange.domain.repository.BalanceController
import com.developers.currency_exchange.domain.repository.BalanceObserver
import com.developers.currency_exchange.domain.repository.BalanceVerifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BalanceManager @Inject constructor(
    private val roundingSetup: RoundingSetup
) : BalanceController, BalanceVerifier, BalanceObserver, BalanceChanger {

    private val balances = MutableStateFlow<LinkedHashMap<String, BigDecimal>>(
        LinkedHashMap<String, BigDecimal>().apply {
            put(DEFAULT_NOT_EMPTY_BALANCE_NAME, DEFAULT_NOT_EMPTY_BALANCE_VALUE)
        }
    )
    private val mutex = Mutex()

    override fun subscribe(): Flow<Map<String, BigDecimal>> = balances.asStateFlow()

    override suspend fun addBalances(names: Map<String, BigDecimal>) {
        mutex.withLock {
            val currentBalances = balances.value
            val updatedBalances = LinkedHashMap(currentBalances).apply {
                for (name in names) {
                    putIfAbsent(name.key, name.value)
                }
            }
            balances.emit(updatedBalances)
        }
    }

    override suspend fun doTransaction(
        sell: String,
        sellAmount: BigDecimal,
        receive: String,
        receiveAmount: BigDecimal
    ) {
        mutex.withLock {
            val sellBalance = balances.value.getOrDefault(sell, DEFAULT_EMPTY_BALANCE)
            val receiveBalance = balances.value.getOrDefault(receive, DEFAULT_EMPTY_BALANCE)

            val updatedSellBalance = sellBalance.minus(sellAmount)
            val updatedReceiveBalance = receiveBalance.plus(receiveAmount)

            val currentBalances = balances.value
            val updatedBalances = LinkedHashMap(currentBalances).apply {
                put(sell, updatedSellBalance)
                put(receive, updatedReceiveBalance)
            }
            balances.emit(updatedBalances)
        }
    }

    override suspend fun isEnoughBalance(name: String, amount: BigDecimal): Boolean {
        mutex.withLock {
            val balance = balances.value.getOrDefault(name, DEFAULT_EMPTY_BALANCE)
            val remainder = balance
                .minus(amount)
                .setScale(roundingSetup.getRoundingCountSign())
            return remainder >= MIN_AVAILABLE_AMOUNT
        }
    }

    companion object {
        private val MIN_AVAILABLE_AMOUNT = BigDecimal.ZERO
        private val DEFAULT_EMPTY_BALANCE = BigDecimal.ZERO
        private const val DEFAULT_NOT_EMPTY_BALANCE_NAME = "EUR"
        private val DEFAULT_NOT_EMPTY_BALANCE_VALUE = BigDecimal.valueOf(1000)
    }
}