package com.developers.currency_exchange.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developers.currency_exchange.core.Logger
import com.developers.currency_exchange.core.setup.RoundingSetup
import com.developers.currency_exchange.domain.model.Rate
import com.developers.currency_exchange.domain.repository.BalanceController
import com.developers.currency_exchange.domain.repository.BalanceObserver
import com.developers.currency_exchange.domain.use_case.ConverterUseCase
import com.developers.currency_exchange.domain.use_case.CurrencyRateUseCase
import com.developers.currency_exchange.domain.use_case.ExchangerUseCase
import com.developers.currency_exchange.domain.use_case.InputValidatorUseCase
import com.developers.currency_exchange.presentation.home.mapper.mapToBalances
import com.developers.currency_exchange.presentation.home.mapper.mapToBalancesUi
import com.developers.currency_exchange.presentation.home.mvi.HomeEffect
import com.developers.currency_exchange.presentation.home.mvi.HomeIntent
import com.developers.currency_exchange.presentation.home.mvi.HomeState
import com.developers.currency_exchange.presentation.model.AmountStatus
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
import java.math.BigDecimal
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val currencyRateUseCase: CurrencyRateUseCase,
    private val logger: Logger,
    private val exchangerUseCase: ExchangerUseCase,
    private val balanceObserver: BalanceObserver,
    private val balanceController: BalanceController,
    private val converterUseCase: ConverterUseCase,
    private val inputValidatorUseCase: InputValidatorUseCase,
    private val roundingSetup: RoundingSetup,
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
                        val currentState = _state.value
                        _state.emit(
                            currentState.copy(
                                currencyRate = currencyRate,
                                sellCurrencies = rates
                                    .filter {
                                        it != currentState.sellSelectedCurrency
                                    },
                                receiveCurrencies = rates
                                    .filter { it != currentState.receiveSelectedCurrency }
                            )
                        )
                    }
                    .catch {
                        _effects.emit(HomeEffect.ShowError(HomeError.NO_CURRENCY_RATE_UPDATES))
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

    override fun exchangeMoney() {
        viewModelScope.launch {
            val localState = _state.value
            val amount = convertAmount(localState.sellAmount)

            if (amount <= BigDecimal.ZERO) {
                _effects.emit(HomeEffect.ShowError(HomeError.EXCHANGE_ZERO))
                return@launch
            }

            val sell: Rate = localState.sellSelectedCurrency
            val receive: Rate = localState.receiveSelectedCurrency

            try {
                val successExchange = exchangerUseCase.exchangeMoney(
                    sell = sell,
                    receive = receive,
                    amount = amount,
                    base = localState.currencyRate.base
                )
                _effects.emit(HomeEffect.ShowSuccessExchange(successExchange))
            } catch (e: ExchangerUseCase.NotEnoughMoneyException) {
                _effects.emit(HomeEffect.ShowError(HomeError.NOT_ENOUGH_MONEY))
            }
        }
    }

    override fun onChangeSellAmount(sellAmount: String) {
        viewModelScope.launch {
            val bigDecimalAmount = convertAmount(sellAmount)
            if (inputValidatorUseCase.invoke(bigDecimalAmount)) {
                val localState = _state.value
                val receiverAmount = getReceiverAmount(bigDecimalAmount)
                val receiverAmountStatus = getAmountStatus(receiverAmount)
                _state.emit(
                    localState.copy(
                        sellAmount = sellAmount,
                        receiveAmount = receiverAmount.toString(),
                        receiverAmountStatus = receiverAmountStatus
                    )
                )
            }
        }
    }

    override fun onChangeSellCurrency(sell: Rate) {
        viewModelScope.launch {
            val localState = _state.value
            val bigDecimalAmount = convertAmount(localState.receiveAmount)
            val receiverAmount = getReceiverAmount(bigDecimalAmount)
            val receiverAmountStatus = getAmountStatus(receiverAmount)
            _state.emit(
                localState.copy(
                    sellSelectedCurrency = sell,
                    receiveAmount = receiverAmount.toString(),
                    receiverAmountStatus = receiverAmountStatus
                )
            )
        }
    }

    override fun onChangeReceiveCurrency(receive: Rate) {
        viewModelScope.launch {
            val localState = _state.value
            val bigDecimalAmount = convertAmount(localState.receiveAmount)
            val receiverAmount = getReceiverAmount(bigDecimalAmount)
            val receiverAmountStatus = getAmountStatus(receiverAmount)
            _state.emit(
                localState.copy(
                    receiveSelectedCurrency = receive,
                    receiveAmount = receiverAmount.toString(),
                    receiverAmountStatus = receiverAmountStatus
                )
            )
        }
    }

    private fun getReceiverAmount(sellAmount: BigDecimal): BigDecimal {
        val localState = _state.value
        val sell: Rate = localState.sellSelectedCurrency
        val receive: Rate = localState.receiveSelectedCurrency

        if (sellAmount <= BigDecimal.ZERO) {
            return sellAmount
        }
        return converterUseCase.convert(
            sell = sell,
            receive = receive,
            amount = sellAmount,
            base = localState.currencyRate.base
        )
            .setScale(roundingSetup.getRoundingUiSign())
    }

    private fun getAmountStatus(amount: BigDecimal): AmountStatus {
        return if (amount == BigDecimal.ZERO) {
            AmountStatus.NEUTRAL
        } else if (amount > BigDecimal.ZERO) {
            AmountStatus.POSITIVE
        } else {
            AmountStatus.NEGATIVE
        }
    }

    private fun convertAmount(amount: String): BigDecimal {
        if (amount.isEmpty()) {
            return BigDecimal.ZERO
        }
        val formattedAmount = amount.replace(COMMA_DELIMITER, DOT_DELIMITER)
        return formattedAmount.toBigDecimal()
    }

    companion object {
        private const val DOT_DELIMITER = '.'
        private const val COMMA_DELIMITER = ','
    }
}