package com.developers.currency_exchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.developers.currency_exchange.presentation.home.HomeScreen
import com.developers.currency_exchange.presentation.home.HomeViewModel
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
                HomeScreen(
                    uiState = uiState.value
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