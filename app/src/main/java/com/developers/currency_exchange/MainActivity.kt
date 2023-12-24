package com.developers.currency_exchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
                HomeScreen()
            }
        }
    }
}