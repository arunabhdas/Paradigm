package app.paradigmatic.paradigmaticapp.di

import app.paradigmatic.paradigmaticapp.data.ParadigmaticDatabase
import app.paradigmatic.paradigmaticapp.data.local.LocalDatabase
import app.paradigmatic.paradigmaticapp.data.local.MongoImpl
import app.paradigmatic.paradigmaticapp.data.local.PreferencesImpl
import app.paradigmatic.paradigmaticapp.data.remote.api.CurrencyApiServiceImpl
import app.paradigmatic.paradigmaticapp.data.remote.api.PostApi
import app.paradigmatic.paradigmaticapp.data.room.getRoomDatabase
import app.paradigmatic.paradigmaticapp.domain.CurrencyApiService
import app.paradigmatic.paradigmaticapp.domain.MongoRepository
import app.paradigmatic.paradigmaticapp.domain.PreferencesRepository
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.HomeViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.MemeViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.PostViewModel
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
    single<LocalDatabase> { LocalDatabase(
        databaseDriverFactory = get()
    ) }
    single<Settings> { Settings() }

    single<ParadigmaticDatabase> {
        ParadigmaticDatabase(
            api = get(),
            database = get(),
            settings = get()
        )
    }
    factory { PostViewModel(
            database = get()
        )
    }

    single { getRoomDatabase(
            builder = get()
        )
    }

    factory {
        MemeViewModel(
            database = get()
        )
    }
}

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}