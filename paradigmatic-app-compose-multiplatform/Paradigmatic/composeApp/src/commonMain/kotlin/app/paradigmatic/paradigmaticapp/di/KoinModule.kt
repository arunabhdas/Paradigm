package app.paradigmatic.paradigmaticapp.di

import app.paradigmatic.paradigmaticapp.data.ParadigmaticDatabaseSdk
import app.paradigmatic.paradigmaticapp.data.local.DatabaseDriverFactory
import app.paradigmatic.paradigmaticapp.data.local.MongoImpl
import app.paradigmatic.paradigmaticapp.data.local.PreferencesImpl
import app.paradigmatic.paradigmaticapp.data.remote.api.CurrencyApiServiceImpl
import app.paradigmatic.paradigmaticapp.data.remote.api.PostApi
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
import app.paradigmatic.paradigmaticapp.domain.MongoRepository
import app.paradigmatic.paradigmaticapp.domain.PreferencesRepository
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.HomeViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.MainViewModel
import org.koin.dsl.module
import com.russhwolf.settings.Settings
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

expect val targetModule: Module

val sharedModule = module {
    single { Settings() }
    single<MongoRepository> { MongoImpl() }
    single<PreferencesRepository> { PreferencesImpl(settings = get()) }
    single<CurrencyApiService> { CurrencyApiServiceImpl(preferences = get() )}

    factory {
        HomeViewModel(
            preferences = get(),
            mongoDb = get(),
            api = get()
        )
    }

    single<PostApi> { PostApi() }
    single<Settings> { Settings() }

    single<ParadigmaticDatabaseSdk> {
        ParadigmaticDatabaseSdk(
            api = get(),
            database = get(),
            settings = get()
        )
    }
    factory { MainViewModel(
            sdk = get()
        )
    }
}

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}