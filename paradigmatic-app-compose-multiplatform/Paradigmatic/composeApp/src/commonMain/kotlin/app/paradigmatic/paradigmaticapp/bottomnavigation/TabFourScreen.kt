package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.domain.model.DisplayResult
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.MemeViewModel
import app.paradigmatic.paradigmaticapp.ui.theme.surfaceContainerDark
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent
import app.paradigmatic.paradigmaticapp.presentation.screen.LoadingView
import app.paradigmatic.paradigmaticapp.presentation.screen.ErrorView
import androidx.compose.foundation.lazy.LazyColumn
import app.paradigmatic.paradigmaticapp.presentation.meme.MemeView
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TabFourScreen(
): Screen, KoinComponent {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()

        val viewModel = koinViewModel<MemeViewModel>()
        val memes by viewModel.memes
        val sortedByFavorite = viewModel.sortedByFavorite


        Scaffold (
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navigator?.push(TabFourScreenManage(number = -1))
                }){
                    Icon(
                        modifier = Modifier.alpha(if(sortedByFavorite.value) 1f else 0.38f),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Sorting Icon"
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navigator?.push(TabFourScreenManage(
                                number = -1
                            ))
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .background(surfaceContainerDark)
                    ) {
                        Text(text = "Management Dashboard")
                    }
                    Button(
                        onClick = {
                            if (memes.isSucces() && memes.getSuccessData().size >= 2) {
                                viewModel.toggleSortByFavorite()
                                scope.launch {
                                    delay(100)
                                    listState.animateScrollToItem(0)
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .background(surfaceContainerDark)
                    ) {
                        Icon(
                            modifier = Modifier.alpha(if(sortedByFavorite.value) 1f else 0.38f),
                            imageVector = Icons.Default.Star,
                            contentDescription = "Sorting Icon"
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    memes.DisplayResult(
                        onLoading = { LoadingView() },
                        onError = { ErrorView(it) },
                        onSuccess = { data ->
                            if (data.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier
                                        .padding(all = 12.dp)
                                        .padding(
                                            top = it.calculateTopPadding(),
                                            bottom = it.calculateBottomPadding()
                                        ),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ){
                                    items(
                                        items = data,
                                        key = { meme ->
                                            meme._id
                                        }
                                    ) { meme ->
                                        ElevatedCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable{
                                                    navigator?.push(TabFourScreenDetail(
                                                        number = meme._id,
                                                        onEditClick = {},
                                                        onBackClick = {}
                                                        )
                                                    )
                                                },
                                            elevation = CardDefaults.elevatedCardElevation(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            MemeView(
                                                meme = meme,
                                                onClick = { navigator?.push(TabFourScreenDetail(
                                                    number = meme._id,
                                                    onEditClick = {},
                                                    onBackClick = {}
                                                    )
                                                )
                                            }
                                            )
                                        }

                                    }
                                }
                            } else {
                                ErrorView()
                            }
                        }
                    )
                }
            }
        }

    }

}

