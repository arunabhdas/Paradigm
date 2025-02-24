package app.paradigmatic.paradigmaticapp.data.local

import app.cash.sqldelight.db.SqlDriver
import app.paradigmatic.paradigmaticapp.ParadigmaticDatabase
import app.paradigmatic.paradigmaticapp.domain.model.Post

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

class LocalDatabase(
    databaseDriverFactory: DatabaseDriverFactory
) {
    private val database = ParadigmaticDatabase(
        databaseDriverFactory.createDriver()
    )
    private val query = database.paradigmaticDatabaseQueries

    fun readAllPosts(): List<Post> {
        println("INFO: Read the cached data from the local database")
        return query.readAlllPosts()
            .executeAsList()
            .map {
                Post(
                    userId = it.userId.toInt(),
                    id = it.id.toInt(),
                    title = it.title,
                    body = it.body
                )
            }
    }

    fun insertAllPosts(posts: List<Post>) {
        println("INFO: Caching the data from the network")
        query.transaction {
            posts.forEach { post ->
                query.insertPost(
                    userId = post.userId.toLong(),
                    id = post.id.toLong(),
                    title = post.title,
                    body = post.body
                )
            }
        }

    }

    fun removeAllPosts() {
        query.removeAllPosts()
    }
}