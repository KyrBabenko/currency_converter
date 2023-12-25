package com.developers.currency_exchange.core.setup

import javax.inject.Inject

interface CommissionSetup {

    fun getCommissionPercentage(): Int

    fun getFreeTransactionsAmount(): Int

    class Default @Inject constructor(): CommissionSetup {

        override fun getCommissionPercentage(): Int = COMMISSION_AMOUNT_AS_PERCENTAGE

        override fun getFreeTransactionsAmount(): Int = FREE_TRANSACTIONS_AMOUNT

        companion object {
            private const val COMMISSION_AMOUNT_AS_PERCENTAGE = 7
            private const val FREE_TRANSACTIONS_AMOUNT = 5
        }
    }
}