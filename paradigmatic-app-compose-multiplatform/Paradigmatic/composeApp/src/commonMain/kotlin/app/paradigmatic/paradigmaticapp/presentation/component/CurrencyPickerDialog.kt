package app.paradigmatic.paradigmaticapp.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyType
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyCode
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import app.paradigmatic.paradigmaticapp.ui.theme.surfaceColor
import app.paradigmatic.paradigmaticapp.ui.theme.textColor



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerDialog (
    currencies: List<Currency>,
    currencyType: CurrencyType,
    onPositiveClick: (CurrencyCode) -> Unit,
    onDismiss: () -> Unit
) {
    val allCurrencies = remember {
        mutableStateListOf<Currency>().apply {
            addAll(currencies)
        }
    }

    var searchQuery by remember { mutableStateOf("") }

    var selectedCurrencyCode by remember(currencyType) {
        mutableStateOf(currencyType.code)
    }

    AlertDialog(
        containerColor = surfaceColor,
        title = {
            Text(
                text = "Select a currency",
                color = textColor
            )
        },
        text = {

        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.outline
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Confirm",
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

    )
}