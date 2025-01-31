package app.paradigmatic.paradigmaticapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.paradigmatic.paradigmaticapp.data.gattble.manager.PropzProBeaconReceiveManager
import app.paradigmatic.paradigmaticapp.data.gattble.model.ConnectionState
import app.paradigmatic.paradigmaticapp.data.gattble.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(
    private val xlr8BeaconReceiveManager: PropzProBeaconReceiveManager
): ViewModel(){

    // [COARSE_LOCATION, FINE_LOCATION, FOREGROUND_SERVICE_LOCATION]
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var proximity by mutableStateOf(0f)
        private set

    var rssi by mutableStateOf(0f)
        private set

    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)


    fun dismissDialog() {
        if (visiblePermissionDialogQueue.size > 0) {
            visiblePermissionDialogQueue.removeFirst()
        }
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    private fun subscribeToChanges() {
        viewModelScope.launch {
            xlr8BeaconReceiveManager.data.collect { result ->
                when(result) {
                    is Resource.Success -> {
                        connectionState = result.data.connectionState
                        proximity = result.data.proximity
                        rssi = result.data.rssi
                    }

                    is Resource.Loading -> {
                        initializingMessage = result.message
                        connectionState = ConnectionState.CurrentlyInitializing
                    }

                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    open fun disconnect() {
        xlr8BeaconReceiveManager.disconnect()
    }

    open fun reconnect() {
        xlr8BeaconReceiveManager.reconnect()
    }

    open fun initializeConnection() {
        errorMessage = null
        subscribeToChanges()
        xlr8BeaconReceiveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        xlr8BeaconReceiveManager.closeConnection()
    }
}