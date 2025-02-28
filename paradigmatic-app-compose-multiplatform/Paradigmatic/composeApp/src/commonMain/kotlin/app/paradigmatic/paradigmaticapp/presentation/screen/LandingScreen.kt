package app.paradigmatic.paradigmaticapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.bottomnavigation.BottomNavigationMainScreen
import app.paradigmatic.paradigmaticapp.ui.theme.headerColor
import app.paradigmatic.paradigmaticapp.ui.theme.primaryContainerDark
import app.paradigmatic.paradigmaticapp.ui.theme.primaryDark
import app.paradigmatic.paradigmaticapp.ui.theme.secondaryContainerDark
import app.paradigmatic.paradigmaticapp.ui.theme.secondaryDark
import app.paradigmatic.paradigmaticapp.ui.theme.surfaceContainerDark
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator


class LandingScreen(): Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var text by remember {
            mutableStateOf("")
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceContainerDark),

            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
            ) {
                Text(
                    text = "Paradigmatic",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                /* TODO-FIXME-CLEANUP
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                */
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        navigator?.push(BottomNavigationMainScreen())
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .background(surfaceContainerDark)
                ) {
                    Text(text = "Get Started")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}
