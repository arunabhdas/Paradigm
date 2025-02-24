package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
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