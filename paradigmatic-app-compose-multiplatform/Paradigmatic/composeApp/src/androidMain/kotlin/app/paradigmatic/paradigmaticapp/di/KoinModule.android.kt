package app.paradigmatic.paradigmaticapp.di

import androidx.lifecycle.viewmodel.compose.viewModel
import app.paradigmatic.paradigmaticapp.ParadigmaticDatabase
import app.paradigmatic.paradigmaticapp.data.ParadigmaticDatabaseSdk
import app.paradigmatic.paradigmaticapp.data.local.AndroidDatabaseDriverFactory
import app.paradigmatic.paradigmaticapp.data.local.DatabaseDriverFactory
import app.paradigmatic.paradigmaticapp.data.remote.api.PostApi
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.MainViewModel
import com.russhwolf.settings.Settings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }
}