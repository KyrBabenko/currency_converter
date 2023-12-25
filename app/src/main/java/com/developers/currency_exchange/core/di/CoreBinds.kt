package com.developers.currency_exchange.core.di

import com.developers.currency_exchange.core.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CoreBinds {

    @Binds
    fun bindLoggerToBase(base: Logger.Base): Logger
}