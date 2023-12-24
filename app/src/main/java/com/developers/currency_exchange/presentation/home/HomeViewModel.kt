package com.developers.currency_exchange.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developers.currency_exchange.domain.use_case.CurrencyRateUseCase
import com.developers.currency_exchange.presentation.home.mvi.HomeEffect
import com.developers.currency_exchange.presentation.home.mvi.HomeIntent
import com.developers.currency_exchange.presentation.home.mvi.HomeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val currencyRateUseCase: CurrencyRateUseCase
) : ViewModel(), HomeIntent {

    init {
        viewModelScope.launch {
            val rates = currencyRateUseCase.invoke()
            Log.d("TAG11", "${rates.entries}")
        }
    }

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

}