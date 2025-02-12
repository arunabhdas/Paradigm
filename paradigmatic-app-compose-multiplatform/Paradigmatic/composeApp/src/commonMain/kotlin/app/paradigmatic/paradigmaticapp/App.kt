package app.paradigmatic.paradigmaticapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.paradigmatic.paradigmaticapp.ui.theme.darkScheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import paradigmatic.composeapp.generated.resources.Res
import paradigmatic.composeapp.generated.resources.compose_multiplatform

import app.paradigmatic.paradigmaticapp.ui.theme.lightScheme
import app.paradigmatic.paradigmaticapp.ui.theme.darkScheme


@Composable
@Preview
fun App() {
    val colors = if (!isSystemInDarkTheme()) {
        lightScheme
    } else {
        darkScheme
    }
    MaterialTheme(colorScheme = colors) {

    }
}