package com.developers.currency_exchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.developers.currency_exchange.presentation.home.HomeScreen
import com.developers.currency_exchange.ui.theme.CurrencyExchangeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyExchangeTheme {
                HomeScreen()
            }
        }
    }
}