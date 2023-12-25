package com.developers.currency_exchange.core.di

import com.developers.currency_exchange.core.setup.CommissionSetup
import com.developers.currency_exchange.core.setup.CurrencySetup
import com.developers.currency_exchange.core.setup.RoundingSetup
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SetupBinds {

    @Binds
    fun bindCurrencySetupToBase(currencySetup: CurrencySetup.Base): CurrencySetup

    @Binds
    fun bindCommissionSetupToBase(commissionSetup: CommissionSetup.Default): CommissionSetup

    @Binds
    fun bindRoundingSetupToBase(roundingSetup: RoundingSetup.Base): RoundingSetup
}