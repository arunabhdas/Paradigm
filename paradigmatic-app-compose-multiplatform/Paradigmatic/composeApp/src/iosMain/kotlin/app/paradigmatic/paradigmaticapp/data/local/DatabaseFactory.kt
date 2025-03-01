package app.paradigmatic.paradigmaticapp.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.paradigmatic.paradigmaticapp.ParadigmaticDatabase
import co.touchlab.sqliter.native.NativeStatement

// SQLDelight & Ktor (Compose Multiplatform)
class IOSDatabaseDriverFactory(): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            ParadigmaticDatabase.Schema,
            "paradigmatic.db"
        )
    }
}