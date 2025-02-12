package app.paradigmatic.paradigmaticapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform