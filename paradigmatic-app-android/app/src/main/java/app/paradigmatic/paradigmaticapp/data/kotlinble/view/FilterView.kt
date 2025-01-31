
package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.data.kotlinble.repository.DevicesScanFilter
import app.paradigmatic.paradigmaticapp.ui.theme.Xlr8ProTheme
import app.paradigmatic.paradigmaticapp.R

@Composable
internal fun FilterView(
    config: DevicesScanFilter,
    onChanged: (DevicesScanFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier,
    ) {
        config.filterUuidRequired?.let {
            ElevatedFilterChip(
                selected = !it,
                onClick = { onChanged(config.copy(filterUuidRequired = !it)) },
                label = { Text(text = stringResource(id = R.string.filter_uuid),) },
                modifier = Modifier.padding(end = 8.dp),
                leadingIcon = {
                    if (!it) {
                        Icon(Icons.Default.Done, contentDescription = "")
                    } else {
                        Icon(Icons.Default.Widgets, contentDescription = "")
                    }
                },
            )
        }
        config.filterNearbyOnly.let {
            ElevatedFilterChip(
                selected = it,
                onClick = { onChanged(config.copy(filterNearbyOnly = !it)) },
                label = { Text(text = stringResource(id = R.string.filter_nearby),) },
                modifier = Modifier.padding(end = 8.dp),
                leadingIcon = {
                    if (it) {
                        Icon(Icons.Default.Done, contentDescription = "")
                    } else {
                        Icon(Icons.Default.Wifi, contentDescription = "")
                    }
                },
            )
        }
        config.filterWithNames.let {
            ElevatedFilterChip(
                selected = it,
                onClick = { onChanged(config.copy(filterWithNames = !it)) },
                label = { Text(text = stringResource(id = R.string.filter_name),) },
                modifier = Modifier.padding(end = 8.dp),
                leadingIcon = {
                    if (it) {
                        Icon(Icons.Default.Done, contentDescription = "")
                    } else {
                        Icon(Icons.AutoMirrored.Filled.Label, contentDescription = "")
                    }
                },
            )
        }
    }
}

@Preview
@Composable
private fun FilterViewPreview() {
    Xlr8ProTheme {
        Column {
            FilterView(
                config = DevicesScanFilter(
                    filterUuidRequired = true,
                    filterNearbyOnly = true,
                    filterWithNames = true,
                ),
                onChanged = {},
                modifier = Modifier.fillMaxWidth(),
            )

            FilterView(
                config = DevicesScanFilter(
                    filterUuidRequired = false,
                    filterNearbyOnly = false,
                    filterWithNames = false,
                ),
                onChanged = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}