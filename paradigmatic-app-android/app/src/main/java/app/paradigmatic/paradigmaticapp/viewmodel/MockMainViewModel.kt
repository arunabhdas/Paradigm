package app.paradigmatic.paradigmaticapp.viewmodel

import app.paradigmatic.paradigmaticapp.data.gattble.manager.MockPropzProBeaconBleReceiveManager


class MockMainViewModel : MainViewModel(MockPropzProBeaconBleReceiveManager()) {

    init {
        // If your Xlr8ProBeaconViewModel has an initialization logic that needs to be triggered
        // for setting up the ViewModel, you can call it here. For example:
        initializeConnection()
    }

    override fun disconnect() {
        // Override with mock behavior if necessary
    }

    override fun reconnect() {
        // Override with mock behavior if necessary
    }

    override fun initializeConnection() {
        // Override with mock behavior if necessary
    }

    // Override any other methods as needed for the preview environment
}
