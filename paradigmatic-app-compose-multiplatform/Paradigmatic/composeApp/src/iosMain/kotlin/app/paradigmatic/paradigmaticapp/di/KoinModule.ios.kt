package app.paradigmatic.paradigmaticapp.di

import app.paradigmatic.paradigmaticapp.data.ParadigmaticDatabaseSdk
import app.paradigmatic.paradigmaticapp.data.local.DatabaseDriverFactory
import app.paradigmatic.paradigmaticapp.data.remote.api.PostApi
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.MainViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module
import app.paradigmatic.paradigmaticapp.data.local.IOSDatabaseDriverFactory
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.dsl.singleOf

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }
}