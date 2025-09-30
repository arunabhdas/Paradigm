package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import android.os.ParcelUuid
import androidx.compose.runtime.*
import app.paradigmatic.paradigmaticapp.ui.navigation.Screen
import no.nordicsemi.android.kotlin.ble.core.scanner.BleScanResults
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber

@Destination
@Composable
fun ScannerView(
    uuid: ParcelUuid?,
    onScanningStateChanged: (Boolean) -> Unit = {},
    onResult: (BleScanResults) -> Unit,
    deviceItem: @Composable (BleScanResults) -> Unit = {
        DeviceListItem(it.advertisedName ?: it.device.name, it.device.address)
    },
    showFilter: Boolean = true,
    navigator: DestinationsNavigator
) {
    LaunchedEffect(key1 = "LandingScreenAppeared") {
        Timber.d( "route: ${Screen.ScannerView.route}")
    }

    /*
    val viewModel = hiltViewModel<ScannerViewModel>()
        .apply { setFilterUuid(uuid) }

    val state by viewModel.state.collectAsStateWithLifecycle(ScanningState.Loading)
    val config by viewModel.filterConfig.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        if (showFilter)
            FilterView(
                config = config,
                onChanged = { viewModel.setFilter(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.appBarColor))
                    .padding(horizontal = 16.dp),
            )

        val pullRefreshState = rememberPullToRefreshState()
        if (pullRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                viewModel.refresh()
                pullRefreshState.endRefresh()
            }
        }

        Box(
            modifier = Modifier
                .nestedScroll(pullRefreshState.nestedScrollConnection)
                .clipToBounds()
        ) {
            if (!pullRefreshState.isRefreshing) {
                DevicesListView(
                    isLocationRequiredAndDisabled = false,
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                    onClick = { onResult(it) },
                    deviceItem = deviceItem,
                )
            }

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullRefreshState,
            )
        }
    }  */
}
