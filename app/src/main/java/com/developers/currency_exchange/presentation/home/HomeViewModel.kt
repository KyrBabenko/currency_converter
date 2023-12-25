package com.developers.currency_exchange.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developers.currency_exchange.core.Logger
import com.developers.currency_exchange.domain.repository.BalanceController
import com.developers.currency_exchange.domain.repository.BalanceObserver
import com.developers.currency_exchange.domain.use_case.CurrencyRateUseCase
import com.developers.currency_exchange.domain.use_case.ExchangerUseCase
import com.developers.currency_exchange.presentation.home.mapper.mapToBalances
import com.developers.currency_exchange.presentation.home.mapper.mapToBalancesUi
import com.developers.currency_exchange.presentation.home.mvi.HomeEffect
import com.developers.currency_exchange.presentation.home.mvi.HomeIntent
import com.developers.currency_exchange.presentation.home.mvi.HomeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val currencyRateUseCase: CurrencyRateUseCase,
    private val logger: Logger,
    private val exchangerUseCase: ExchangerUseCase,
    private val balanceObserver: BalanceObserver,
    private val balanceController: BalanceController,
) : ViewModel(), HomeIntent {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

    init {
        observeCurrencyRateChanges()
    }

    private var currencyRateJob: Job? = null
    private var balancesJob: Job? = null

    override fun observeCurrencyRateChanges() {
        if (currencyRateJob == null) {
            currencyRateJob = viewModelScope.launch {
                currencyRateUseCase.invoke()
                    .onEach { currencyRate ->
                        logger.d("CurrencyRate: $currencyRate")
                        balanceController.addBalances(currencyRate.mapToBalances())
                        val rates = currencyRate.rates
                        val currencies = rates.map { it.name }
                        val currentState = _state.value
                        _state.emit(
                            currentState.copy(
                                cellCurrencies = currencies
                                    .filter {
                                        it != currentState.selectedSellCurrency
                                    },
                                receiveCurrencies = currencies
                                    .filter { it != currentState.selectedReceiveCurrency}
                            )
                        )
                    }
                    .catch {
                        _effects.emit(HomeEffect.NoCurrencyRateUpdates)
                        logger.e("CurrencyRate error: $it")
                    }
                    .shareIn(this, SharingStarted.Eagerly)
            }
        }
        if (balancesJob == null) {
            balancesJob = viewModelScope.launch {
                balanceObserver.subscribe()
                    .onEach {
                        val currentState = _state.value
                        _state.emit(
                            currentState.copy(
                                balances = it.mapToBalancesUi()
                            )
                        )
                    }
                    .shareIn(this, SharingStarted.Eagerly)
            }
        }
    }

    override fun cancelJob() {
        currencyRateJob?.cancel()
        currencyRateJob = null

        balancesJob?.cancel()
        balancesJob = null
    }
}