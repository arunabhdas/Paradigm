package app.paradigmatic.paradigmaticapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.paradigmatic.paradigmaticapp.bottomnavigation.BottomNavigationMainScreen
import app.paradigmatic.paradigmaticapp.di.initializeKoin
import app.paradigmatic.paradigmaticapp.ui.theme.darkScheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import paradigmatic.composeapp.generated.resources.Res
import paradigmatic.composeapp.generated.resources.compose_multiplatform

import app.paradigmatic.paradigmaticapp.ui.theme.lightScheme
import app.paradigmatic.paradigmaticapp.ui.theme.darkScheme
import app.paradigmatic.paradigmaticapp.navigation.MainScreen
import cafe.adriel.voyager.navigator.Navigator

/* TODO-FIXME-CLEANUP
@Composable
@Preview
fun App() {
    val colors = if (!isSystemInDarkTheme()) {
        lightScheme
    } else {
        darkScheme
    }
    MaterialTheme(colorScheme = colors) {
        Surface {
            MainScreen()
        }
    }
}
*/

@Composable
@Preview
fun App() {
    initializeKoin()
    val colors = if (!isSystemInDarkTheme()) {
        // TODO-FIXME
        // TODO-FIXME-Commenting out to force darkScheme
        //  lightScheme
        darkScheme
    } else {
        darkScheme
    }
    MaterialTheme(colorScheme = colors) {
        Surface {
            Navigator(BottomNavigationMainScreen())
        }
    }
}