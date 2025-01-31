package app.paradigmatic.paradigmaticapp.data.gattble.manager

import app.paradigmatic.paradigmaticapp.data.gattble.model.PropzProBeaconResult
import app.paradigmatic.paradigmaticapp.data.gattble.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface PropzProBeaconReceiveManager {
    val data: MutableSharedFlow<Resource<PropzProBeaconResult>>

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()

}