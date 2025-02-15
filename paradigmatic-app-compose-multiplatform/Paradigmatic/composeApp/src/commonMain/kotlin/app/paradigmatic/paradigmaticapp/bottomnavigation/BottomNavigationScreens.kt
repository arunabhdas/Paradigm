package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.data.remote.api.CurrencyApiServiceImpl
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
import app.paradigmatic.paradigmaticapp.domain.PreferencesRepository
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyCode
import app.paradigmatic.paradigmaticapp.domain.model.RateStatus
import app.paradigmatic.paradigmaticapp.domain.model.RequestState
import app.paradigmatic.paradigmaticapp.presentation.component.HomeHeader
import app.paradigmatic.paradigmaticapp.presentation.screen.HomeUiEvent
import app.paradigmatic.paradigmaticapp.presentation.screen.HomeViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import paradigmatic.composeapp.generated.resources.Res
import paradigmatic.composeapp.generated.resources.switch_ic

class TabOneScreen: Screen, KoinComponent {
    private val currencyApiService: CurrencyApiService by inject()
    @Composable
    override fun Content() {
        LaunchedEffect(Unit) {
            println("TabOneScreen")
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
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Markets",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Markets Screen")
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
        val sourceCurrency by viewModel.sourceCurrency
        val targetCurrency by viewModel.targetCurrency
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
                    onRatesRefresh = {
                        viewModel.sendEvent(
                            HomeUiEvent.RefreshRates
                        )
                    },
                    onSwitchClick = {}
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
