package app.paradigmatic.paradigmaticapp.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyType
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyCode
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue



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
}