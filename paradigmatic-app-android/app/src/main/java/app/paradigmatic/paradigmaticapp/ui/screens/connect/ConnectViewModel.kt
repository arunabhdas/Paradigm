package app.paradigmatic.paradigmaticapp.ui.screens.connect

import android.app.Application
import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.paradigmatic.paradigmaticapp.ui.ibeacon.ScanService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(
    application: Application
) : ViewModel() {

    val deviceList = MutableLiveData<List<Any>>() // Replace Any with your device data type
    val isScanning = MutableLiveData<Boolean>(false)
    val buttonText = MutableLiveData<String>("Scan")

    private lateinit var scanService: ScanService
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


    init {
        initializeScanService()
    }

    private fun initializeScanService() {
        // Initialize the device list that will be passed to the ScanService
        val initialDeviceList = arrayListOf<Any>() // Replace 'Any' with your specific device type

        // Initialize ScanService with the context and the device list
        //scanService = ScanService(getApplicati on(), deviceList)

        // Observe changes in the device list and update LiveData accordingly
        // Assuming ScanService updates this list, you might need a way to observe these changes
        // For example, if ScanService provides a callback or a way to observe changes, use it here
    }


    /**
     * Determine whether bluetooth is enabled or not
     *
     * @return true if bluetooth is enabled, false otherwise
     */
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    fun startScan() {
        if (isBluetoothEnabled() == false) {
            // Handle Bluetooth not enabled
            // You can update a LiveData to show a message or open Bluetooth settings
            // For instance, trigger an event that the UI can listen to and show a dialog
            return
        }

        if (isScanning.value == true) {
            stopBLEScan()
        } else {
            isScanning.value = true
            buttonText.value = "Stop"
            viewModelScope.launch(Dispatchers.IO) {
                scanService.startBLEScan()
                // Update device list based on scan results
                // You might want to fetch the results from scanService and post it to deviceList
                // Example: deviceList.postValue(updatedList)
            }
        }
    }

    fun stopBLEScan() {
        isScanning.value = false
        buttonText.value = "Scan"
        viewModelScope.launch(Dispatchers.IO) {
            scanService.stopBLEScan()
            // Handle any additional logic needed when scanning stops
        }
    }


    fun exitApp() {
        // Handle exit logic
    }
}
