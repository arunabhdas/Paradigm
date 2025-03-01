package app.paradigmatic.paradigmaticapp.di

import app.paradigmatic.paradigmaticapp.data.local.DatabaseDriverFactory
import org.koin.dsl.module
import app.paradigmatic.paradigmaticapp.data.local.IOSDatabaseDriverFactory
import app.paradigmatic.paradigmaticapp.data.room.getDatabaseBuilder

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }

    single{ getDatabaseBuilder() }

}