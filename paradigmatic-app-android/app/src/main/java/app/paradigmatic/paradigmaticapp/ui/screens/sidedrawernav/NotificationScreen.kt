package app.paradigmatic.paradigmaticapp.ui.screens.sidedrawernav
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.destinations.LoginScreenDestination
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun NotificationScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel()
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
            Image(
                painter = painterResource(id = R.drawable.appicon),
                contentDescription = stringResource(id = R.string.content_description),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PrimaryColor
                ),
                onClick = {
                    // Clear user data
                    authViewModel.saveUsernameToDataStore("")
                    authViewModel.saveEmailToDataStore("")
                    authViewModel.savePasswordToDataStore("")
                    authViewModel.saveAccessTokenToDataStore("")
                    authViewModel.saveRefreshTokenToDataStore("")
                    authViewModel.saveIsUserLoggedInToDataStore(false)
                    authViewModel.saveIsOnboardingShownToDataStore(false)
                    
                    // Use only Destinations navigation
                    navigator.navigate(
                        LoginScreenDestination(
                            isFromRegistration = true,
                            isManuallyLoggedOut = true
                        )
                    ) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }) {
                Text(
                    text = "Logout",
                    color = Color.White
                )
            }
            Text(
                modifier = Modifier.clickable {
                    // TODO
                },
                text = "No Notifications",
                color = Color.White,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold

            )
        }

    }
}

@Preview
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator(),
        authViewModel = hiltViewModel()
    )
}