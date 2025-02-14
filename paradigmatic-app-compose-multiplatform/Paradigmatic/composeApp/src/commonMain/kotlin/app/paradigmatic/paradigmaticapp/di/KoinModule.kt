package app.paradigmatic.paradigmaticapp.di

import app.paradigmatic.paradigmaticapp.data.local.PreferencesImpl
import app.paradigmatic.paradigmaticapp.data.remote.api.CurrencyApiServiceImpl
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
import app.paradigmatic.paradigmaticapp.domain.PreferencesRepository
import org.koin.dsl.module
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin

val appModule = module {
    single { Settings() }
    single<PreferencesRepository> { PreferencesImpl(settings = get()) }
    single<CurrencyApiService> { CurrencyApiServiceImpl(preferences = get() )}
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}