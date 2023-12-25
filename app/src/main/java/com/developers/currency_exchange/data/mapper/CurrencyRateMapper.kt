package com.developers.currency_exchange.data.mapper

import com.developers.currency_exchange.data.model.RateDTO
import com.developers.currency_exchange.domain.model.CurrencyRate
import com.developers.currency_exchange.domain.model.Rate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd"

fun RateDTO.mapToDomain(): CurrencyRate {

    val formatter = DateTimeFormatter.ofPattern(SERVER_DATE_TIME_FORMAT).withZone(ZoneOffset.UTC)
    val localDate = LocalDate.parse(date, formatter)
    val localTime = LocalTime.now()
    val date = LocalDateTime.of(localDate, localTime)

    return CurrencyRate(
        base = base,
        date = date,
        rates = rates.mapToRate()
    )
}

fun Map<String, Float>.mapToRate(): List<Rate> {
    return map {
        Rate(name = it.key, rate = it.value)
    }
}