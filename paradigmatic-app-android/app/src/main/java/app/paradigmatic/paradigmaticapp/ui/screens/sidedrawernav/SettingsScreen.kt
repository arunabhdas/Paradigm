package app.paradigmatic.paradigmaticapp.ui.screens.sidedrawernav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.ui.screens.composables.Xlr8ProActionItem
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import timber.log.Timber

@Destination
@Composable
fun SettingsScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Xlr8ProActionItem(
                imageId = R.drawable.appicon,
                text = "Start Scan",
                contentDescription = stringResource(id = R.string.content_description)
            ) {
                // TODO: Handle Report Incident Click
                Timber.d( "Clicked Report Incident")
            }
        }

    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}