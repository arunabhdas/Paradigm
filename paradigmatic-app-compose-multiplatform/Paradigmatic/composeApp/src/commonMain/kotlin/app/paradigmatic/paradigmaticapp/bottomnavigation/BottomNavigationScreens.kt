package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
import app.paradigmatic.paradigmaticapp.presentation.component.HomeBody
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyType
import app.paradigmatic.paradigmaticapp.presentation.component.CurrencyPickerDialog
import app.paradigmatic.paradigmaticapp.presentation.component.HomeHeader
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.HomeUiEvent
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.HomeViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.PostViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TabOneScreen: Screen, KoinComponent {
    private val currencyApiService: CurrencyApiService by inject()
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<PostViewModel>()
        val allPosts by viewModel.allPosts
        LaunchedEffect(Unit) {
            println("TabOneScreen")
            currencyApiService.getLatestExchangeRates()
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (allPosts.isSuccess()) {
                    val data = remember { allPosts.getSuccessData() }
                    LazyColumn {
                        items(
                            items = data,
                            key = { it.id }
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                text = "(${it.id} - ${it.title})",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                text = it.body,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                        }
                    }
                } else if (allPosts.isError()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 48.dp),
                            contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = allPosts.getErrorMessage(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }
    }
}



@Composable
fun TabTwoScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = "Trending",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Trending Screen")
        }
    }
}

class TabThreeScreen: Screen, KoinComponent {
    private val currencyApiService: CurrencyApiService by inject()
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeViewModel>()
        val rateStatus by viewModel.rateStatus
        val allCurrencies = viewModel.allCurrencies
        val sourceCurrency by viewModel.sourceCurrency
        val targetCurrency by viewModel.targetCurrency
        val sourceCurrencyDisplay by viewModel.sourceCurrencyDisplay
        val targetCurrencyDisplay by viewModel.targetCurrencyDisplay
        var amount by rememberSaveable { mutableStateOf(0.0) }

        var selectedCurrencyType: CurrencyType by remember {
            mutableStateOf(CurrencyType.None)
        }

        var dialogOpened by remember { mutableStateOf(false) }

        if (dialogOpened && selectedCurrencyType != CurrencyType.None) {
            CurrencyPickerDialog(
                currencies = allCurrencies,
                currencyType = selectedCurrencyType,
                onConfirmClick = { currencyCode ->
                    if (selectedCurrencyType is CurrencyType.Source) {
                        println("---------source currencyCode ${currencyCode}")
                        viewModel.sendEvent(
                            HomeUiEvent.SaveSourceCurrencyCode(
                                code = currencyCode.name
                            )
                        )
                    } else if (selectedCurrencyType is CurrencyType.Target) {
                        println("---------target currencyCode ${currencyCode}")
                        viewModel.sendEvent(
                            HomeUiEvent.SaveTargetCurrencyCode(
                                code = currencyCode.name
                            )
                        )
                    }

                    selectedCurrencyType = CurrencyType.None
                    dialogOpened = false
                },
                onDismiss = {
                    selectedCurrencyType = CurrencyType.None
                    dialogOpened = false
                }
            )
        }

        LaunchedEffect(Unit) {
            println("TabThreeScreen")
            currencyApiService.getLatestExchangeRates()
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HomeHeader(
                    status = rateStatus,
                    source = sourceCurrency,
                    target = targetCurrency,
                    amount = amount,
                    onAmountChange = { amount = it },
                    onRatesRefresh = {
                        viewModel.sendEvent(
                            HomeUiEvent.RefreshRates
                        )
                    },
                    onSwitchClick = {
                        println("onSwitchClick triggered on TabThreeScreen")
                        viewModel.sendEvent(
                            HomeUiEvent.SwitchCurrencies
                        )
                    },
                    onCurrencyTypeSelect = { currencyType ->
                        selectedCurrencyType = currencyType
                        dialogOpened = true
                    }
                )
                HomeBody(
                    source = sourceCurrency,
                    target = targetCurrency,
                    amount = amount,
                    sourceDisplay = sourceCurrencyDisplay,
                    targetDisplay = targetCurrencyDisplay
                )
            }
        }
    }
}

@Composable
fun TabFourScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Bookmarks",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Alerts Screen")
        }
    }
}

@Composable
fun TabFiveScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("More Screen")
        }
    }
}
