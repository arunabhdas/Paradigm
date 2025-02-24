package app.paradigmatic.paradigmaticapp.di

import app.paradigmatic.paradigmaticapp.data.local.AndroidDatabaseDriverFactory
import app.paradigmatic.paradigmaticapp.data.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }
}