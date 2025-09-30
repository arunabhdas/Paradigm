package app.paradigmatic.paradigmaticapp.data.kotlinble.view


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.BluetoothSearching
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.ui.theme.Xlr8ProTheme

@Composable
internal fun ScanErrorView(
    error: Int,
) {
    WarningView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        imageVector = Icons.AutoMirrored.Filled.BluetoothSearching,
        title = stringResource(id = R.string.scanner_error),
        hint = stringResource(id = R.string.scan_failed, error),
    )
}

@Preview
@Composable
private fun ErrorSectionPreview() {
    Xlr8ProTheme {
        ScanErrorView(3)
    }
}