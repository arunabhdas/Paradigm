package app.paradigmatic.paradigmaticapp

import androidx.compose.ui.window.ComposeUIViewController
import app.paradigmatic.paradigmaticapp.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }