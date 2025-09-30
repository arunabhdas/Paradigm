package app.paradigmatic.paradigmaticapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import app.paradigmatic.paradigmaticapp.services.BeaconProToolsService

class ParadigmaticAppBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val startServiceIntent = Intent(context, BeaconProToolsService::class.java)
        context.startService(startServiceIntent)
    }
}