package app.paradigmatic.paradigmaticapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class PropzProAppApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "ibeacon_scanning_channel",
                "Scanning Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



    }
}