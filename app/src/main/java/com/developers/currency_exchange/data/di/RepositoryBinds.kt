package com.developers.currency_exchange.data.di

import com.developers.currency_exchange.data.repository.BalanceManager
import com.developers.currency_exchange.data.repository.Commission
import com.developers.currency_exchange.data.repository.RatesRepositoryInMemory
import com.developers.currency_exchange.domain.repository.BalanceChanger
import com.developers.currency_exchange.domain.repository.BalanceController
import com.developers.currency_exchange.domain.repository.BalanceObserver
import com.developers.currency_exchange.domain.repository.BalanceVerifier
import com.developers.currency_exchange.domain.repository.CommissionApplier
import com.developers.currency_exchange.domain.repository.CommissionCounter
import com.developers.currency_exchange.domain.repository.RatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBinds {

    @Binds
    fun bindCurrencyRepository(currencyRepositoryInMemory: RatesRepositoryInMemory): RatesRepository

    @Binds
    fun bindBalanceManagerToBalanceVerifier(balanceManager: BalanceManager): BalanceVerifier

    @Binds
    fun bindBalanceManagerToBalanceController(balanceManager: BalanceManager): BalanceController

    @Binds
    fun bindBalanceManagerToBalanceObserver(balanceManager: BalanceManager): BalanceObserver

    @Binds
    fun bindBalanceManagerToBalanceChanger(balanceManager: BalanceManager): BalanceChanger

    @Binds
    fun bindCommissionToCommissionCounter(commission: Commission): CommissionCounter

    @Binds
    fun bindCommissionToCommissionApplier(commission: Commission): CommissionApplier
}