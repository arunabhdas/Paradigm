package app.paradigmatic.paradigmaticapp.data.gattble.manager

import app.paradigmatic.paradigmaticapp.data.gattble.model.PropzProBeaconResult
import app.paradigmatic.paradigmaticapp.data.gattble.model.ConnectionState
import app.paradigmatic.paradigmaticapp.data.gattble.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MockPropzProBeaconBleReceiveManager : PropzProBeaconReceiveManager {

    override val data: MutableSharedFlow<Resource<PropzProBeaconResult>> = MutableSharedFlow()

    // Create a CoroutineScope for the mock manager
    private val scope = CoroutineScope(Dispatchers.Main)
    override fun startReceiving() {
        // Emit a sample data for preview
        scope.launch {

            data.emit(Resource.Loading(message = "Mock: Scanning for iBeacons..."))

            // Simulate a successful connection with mock data
            data.emit(
                Resource.Success(
                    PropzProBeaconResult(
                        proximity = 1.5f,             // Mock proximity
                        timestamp = System.currentTimeMillis().toFloat(), // Current timestamp
                        rssi = -70f,                  // Mock RSSI value
                        region = "MockRegion",        // Mock region name
                        accuracy = 2.0f,              // Mock accuracy
                        major = 100f,                 // Mock major value
                        uuid = "123e4567-e89b-12d3-a456-426614174000", // Mock UUID
                        minor = 200f,                 // Mock minor value
                        connectionState = ConnectionState.Connected // Mock connection state
                    )
                )
            )

        }
    }

    override fun reconnect() {
        // No operation for preview
    }

    override fun disconnect() {
        // No operation for preview
    }

    override fun closeConnection() {
        // No operation for preview
    }

    // Include any other methods from the interface that need to be mocked
}
