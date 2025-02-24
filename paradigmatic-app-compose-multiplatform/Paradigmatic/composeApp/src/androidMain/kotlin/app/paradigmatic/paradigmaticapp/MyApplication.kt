package app.paradigmatic.paradigmaticapp

import android.app.Application
import app.paradigmatic.paradigmaticapp.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = { androidContext(this@MyApplication )}
        )
    }
}