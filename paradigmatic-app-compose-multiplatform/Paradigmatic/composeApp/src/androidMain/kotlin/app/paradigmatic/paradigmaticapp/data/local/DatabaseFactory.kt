package app.paradigmatic.paradigmaticapp.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.paradigmatic.paradigmaticapp.ParadigmaticDatabase

class DatabaseFactory (
    private val context: Context
): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            ParadigmaticDatabase.Schema,
            context,
            "paradigmatic.db"
        )
    }
}