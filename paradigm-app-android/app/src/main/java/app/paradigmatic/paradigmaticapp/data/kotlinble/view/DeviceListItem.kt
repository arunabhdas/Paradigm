package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.ui.theme.Xlr8ProTheme
import app.paradigmatic.paradigmaticapp.R

@Composable
fun DeviceListItem(
    name: String?,
    address: String,
    modifier: Modifier = Modifier,
    extras: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically)
    {
        CircularIcon(Icons.Default.Bluetooth)

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            name?.takeIf { it.isNotEmpty() }?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
            } ?: Text(
                    text = stringResource(id = R.string.device_no_name),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.alpha(0.7f)
                )
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        extras()
    }
}

@Preview
@Composable
private fun DeviceListItemPreview() {
    Xlr8ProTheme {
        DeviceListItem(
            name = "Device name",
            address = "AA:BB:CC:DD:EE:FF",
            extras = {
                RssiIcon(rssi = -45)
            }
        )
    }
}