package com.developers.currency_exchange.data.api

import com.developers.currency_exchange.data.model.RateDTO
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {

    @GET("tasks/api/currency-exchange-rates")
    suspend fun getRate(): Response<RateDTO>
}