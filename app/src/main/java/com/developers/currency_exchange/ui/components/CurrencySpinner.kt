package com.developers.currency_exchange.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySpinner(
    modifier: Modifier = Modifier,
    currencies: List<String>,
    selectedCurrency: String,
    onChangeCurrency: (selectedCurrency: String) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
    ) {
        Row(
            modifier = Modifier.menuAnchor()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 2.dp),
                text = selectedCurrency,
                style = MaterialTheme.typography.bodyLarge
            )
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
        }
        ExposedDropdownMenu(
            modifier = Modifier,
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            for (currency in currencies) {
                DropdownMenuItem(
                    modifier = Modifier,
                    text = {
                        Text(
                            modifier = Modifier.wrapContentWidth(),
                            text = currency,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1
                        )
                    },
                    onClick = {
                        onChangeCurrency(currency)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
