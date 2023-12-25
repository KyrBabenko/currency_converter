package com.developers.currency_exchange

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.developers.currency_exchange.presentation.home.HomeScreen
import com.developers.currency_exchange.presentation.home.HomeViewModel
import com.developers.currency_exchange.presentation.home.mapper.getResource
import com.developers.currency_exchange.presentation.home.mvi.HomeEffect
import com.developers.currency_exchange.ui.theme.CurrencyExchangeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyExchangeTheme {
                val uiState = viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(key1 = Unit) {
                    viewModel.effects.collect { event ->
                        when (event) {
                            is HomeEffect.ShowError -> {
                                val message = resources.getString(event.error.getResource())
                                Toast.makeText(
                                    this@MainActivity,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is HomeEffect.ShowSuccessExchange -> {
                                val message = resources.getString(
                                    R.string.currency_converted_description,
                                    event.successExchange.sellAmount,
                                    event.successExchange.sellCurrency,
                                    event.successExchange.receiveAmount,
                                    event.successExchange.receiveCurrency,
                                    event.successExchange.commissionAmount,
                                    event.successExchange.commissionCurrency,
                                )
                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle(R.string.currency_converted_title)
                                    .setMessage(message)
                                    .setPositiveButton(R.string.currency_converted_button, null)
                                    .show()
                            }
                        }
                    }
                }

                HomeScreen(
                    uiState = uiState.value,
                    intent = viewModel
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelJob()
    }

    override fun onResume() {
        super.onResume()
        viewModel.observeCurrencyRateChanges()
    }
}