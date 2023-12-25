package com.developers.currency_exchange.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developers.currency_exchange.R
import com.developers.currency_exchange.domain.model.Rate
import com.developers.currency_exchange.presentation.model.AmountStatus
import com.developers.currency_exchange.ui.theme.Green80

@Composable
fun CurrencyExchange(
    modifier: Modifier = Modifier,
    sellAmount: String,
    onChangeSellAmount: (selectedCurrency: String) -> Unit,
    sellCurrencies: List<Rate>,
    sellSelectedCurrency: Rate,
    onChangeSellCurrency: (selectedCurrency: Rate) -> Unit,
    receiveAmount: String,
    receiveAmountStatus: AmountStatus,
    receiveCurrencies: List<Rate>,
    selectedReceiveCurrency: Rate,
    onChangeReceiveCurrency: (selectedCurrency: Rate) -> Unit,
    onKeyboardAction: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(top = 20.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.currency_exchange).uppercase(),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        ExchangeSellItem(
            amount = sellAmount,
            onChangeAmount = onChangeSellAmount,
            currencies = sellCurrencies,
            selectedCurrency = sellSelectedCurrency,
            onChangeCurrency = onChangeSellCurrency,
            onKeyboardAction = onKeyboardAction,
        )
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        Divider()
        Spacer(modifier = Modifier.padding(vertical = 5.dp))
        ExchangeReceiveItem(
            amount = receiveAmount,
            amountStatus = receiveAmountStatus,
            currencies = receiveCurrencies,
            selectedCurrency = selectedReceiveCurrency,
            onChangeCurrency = onChangeReceiveCurrency,
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExchangeSellItem(
    modifier: Modifier = Modifier,
    amount: String,
    onChangeAmount: (amount: String) -> Unit,
    currencies: List<Rate>,
    selectedCurrency: Rate,
    onChangeCurrency: (selectedCurrency: Rate) -> Unit,
    onKeyboardAction: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_sell),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(start = 10.dp),
                text = stringResource(id = R.string.sell),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
            )
        }
        Row(
            modifier = Modifier
                .wrapContentSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .wrapContentHeight()
                    .wrapContentWidth(),
                value = amount,
                onValueChange = onChangeAmount,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onKeyboardAction.invoke()
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                singleLine = true,
                maxLines = 1,
                minLines = 1,
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(
                        textAlign = TextAlign.Right
                    ),
            )
            CurrencySpinner(
                modifier = Modifier.padding(top = 2.dp),
                currencies = currencies,
                selectedCurrency = selectedCurrency,
                onChangeCurrency = onChangeCurrency,
            )
        }
    }
}

@Composable
fun ExchangeReceiveItem(
    modifier: Modifier = Modifier,
    amount: String,
    amountStatus: AmountStatus,
    currencies: List<Rate>,
    selectedCurrency: Rate,
    onChangeCurrency: (selectedCurrency: Rate) -> Unit,
) {
    val formattedAmount = when (amountStatus) {
        AmountStatus.POSITIVE -> "+$amount"
        AmountStatus.NEGATIVE -> "-$amount"
        AmountStatus.NEUTRAL -> amount
    }
    val amountColor = when (amountStatus) {
        AmountStatus.POSITIVE -> Green80
        AmountStatus.NEGATIVE -> Color.Red
        AmountStatus.NEUTRAL -> Color.Black
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_receive),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(start = 10.dp),
                text = stringResource(id = R.string.receive),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
            )
        }
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 20.dp),
                text = formattedAmount,
                color = amountColor,
                style = MaterialTheme.typography.bodyLarge,
            )
            CurrencySpinner(
                modifier = Modifier.padding(top = 2.dp),
                currencies = currencies,
                selectedCurrency = selectedCurrency,
                onChangeCurrency = onChangeCurrency,
            )
        }
    }
}
