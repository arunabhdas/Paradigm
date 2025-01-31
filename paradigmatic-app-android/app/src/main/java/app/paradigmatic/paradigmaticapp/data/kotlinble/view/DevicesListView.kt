package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.data.kotlinble.model.ScanningState
import app.paradigmatic.paradigmaticapp.ui.theme.Xlr8ProTheme
import no.nordicsemi.android.kotlin.ble.core.scanner.BleScanResults

@Composable
fun DevicesListView(
    isLocationRequiredAndDisabled: Boolean,
    state: ScanningState,
    onClick: (BleScanResults) -> Unit,
    modifier: Modifier = Modifier,
    deviceItem: @Composable (BleScanResults) -> Unit = {
        DeviceListItem(it.device.name, it.device.address)
    },
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
    ) {
        when (state) {
            is ScanningState.Loading -> item { ScanEmptyView(isLocationRequiredAndDisabled) }
            is ScanningState.DevicesDiscovered -> {
                if (state.isEmpty()) {
                    item { ScanEmptyView(isLocationRequiredAndDisabled) }
                } else {
                    DeviceListItems(state, onClick, deviceItem)
                }
            }
            is ScanningState.Error -> item { ScanErrorView(state.errorCode) }
        }
    }
}

@Preview(name = "Location required")
@Composable
private fun DeviceListView_Preview_LocationRequired() {
    Xlr8ProTheme {
        DevicesListView(
            isLocationRequiredAndDisabled = true,
            state = ScanningState.Loading,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun DeviceListView_Preview_LocationNotRequired() {
    Xlr8ProTheme {
        DevicesListView(
            isLocationRequiredAndDisabled = false,
            state = ScanningState.Loading,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun DeviceListView_Preview_Error() {
    Xlr8ProTheme {
        DevicesListView(
            isLocationRequiredAndDisabled = true,
            state = ScanningState.Error(1),
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun DeviceListView_Preview_Empty() {
    Xlr8ProTheme {
        DevicesListView(
            isLocationRequiredAndDisabled = true,
            state = ScanningState.DevicesDiscovered(emptyList()),
            onClick = {}
        )
    }
}
