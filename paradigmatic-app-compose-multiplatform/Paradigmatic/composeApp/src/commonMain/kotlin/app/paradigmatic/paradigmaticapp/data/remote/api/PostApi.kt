package app.paradigmatic.paradigmaticapp.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import app.paradigmatic.paradigmaticapp.domain.model.Post
import io.ktor.client.call.body
import io.ktor.client.request.get

const val ALLPOSTS_ENDPOINT = "https://jsonplaceholder.typicode.com/"
class PostApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun fetchAllPosts(): List<Post> {
        println("INFO: Fetching new posts from the network")
        return httpClient.get(urlString = ALLPOSTS_ENDPOINT).body()
    }
}