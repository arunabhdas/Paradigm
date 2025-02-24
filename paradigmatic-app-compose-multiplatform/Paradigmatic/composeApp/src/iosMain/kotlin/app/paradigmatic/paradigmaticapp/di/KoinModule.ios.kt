package app.paradigmatic.paradigmaticapp.di

import app.paradigmatic.paradigmaticapp.data.local.DatabaseDriverFactory
import org.koin.dsl.module
import app.paradigmatic.paradigmaticapp.data.local.IOSDatabaseDriverFactory

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }
}