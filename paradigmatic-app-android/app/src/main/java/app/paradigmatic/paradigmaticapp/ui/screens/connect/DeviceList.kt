package app.paradigmatic.paradigmaticapp.ui.screens.connect

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun DeviceList(deviceList: List<Any>) {
    LazyColumn {
        items(deviceList) { device ->
            // Replace with your device item composable
            DeviceItem(device)
        }
    }
}

@Composable
fun DeviceItem(device: Any) {
    // Define how each device should be displayed
    Text(device.toString()) // Placeholder, replace with actual implementation
}