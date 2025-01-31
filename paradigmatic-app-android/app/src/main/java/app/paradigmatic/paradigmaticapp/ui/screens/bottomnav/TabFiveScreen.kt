package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.destinations.ScanQrCodeScreenDestination
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.ui.components.AboutDialog
import app.paradigmatic.paradigmaticapp.ui.screens.CustomScreenBar
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.TertiaryColor
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TabFiveScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showAboutDialog = remember { mutableStateOf(false) }

    val username by authViewModel.username.collectAsState()

    val isOnboardingShown by authViewModel.isOnboardingShown.collectAsState()

    val refreshToken by authViewModel.refreshToken.collectAsState()
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
        if (showAboutDialog.value) {
            AboutDialog(
                title = stringResource(id = R.string.app_about_title),
                description = stringResource(id = R.string.app_about_description),
                navigator = navigator,
                onDismiss = {},
                onBottomButtonTapped = {
                    showAboutDialog.value = false
                }
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Adjust top padding as needed
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CustomScreenBar(
                        stringResource(R.string.tab_5_name),
                        {
                            showAboutDialog.value = true
                        },
                        {
                            PermUtils.openAppSettings(context)
                        }
                    )
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    )
                    {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            thickness = 1.dp,
                            color = TertiaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.appicon),
                            contentDescription = stringResource(id = R.string.content_description),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(56.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.clickable {
                                // TODO
                            },
                            text = "Welcome ${username}",
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Bold

                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = PrimaryColor
                            ),
                            onClick = {
                                navigator.navigate(ScanQrCodeScreenDestination)
                            }) {
                            Text(
                                text = "Scan Code",
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = PrimaryColor
                            ),
                            onClick = {
                                PermUtils.openAppSettings(context)
                            }) {
                            Text(
                                text = "Manage Permissions",
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = PrimaryColor
                            ),
                            onClick = {
                                PermUtils.openLocationSettings(context)
                            }) {
                            Text(
                                text = "Manage Location Settings",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = PrimaryColor
                            ),
                            onClick = {
                                // TODO-FIXME-IMPLEMENT
                                authViewModel.saveUsernameToDataStore("")
                                authViewModel.saveEmailToDataStore("")
                                authViewModel.savePasswordToDataStore("")
                                authViewModel.saveAccessTokenToDataStore("")
                                authViewModel.saveRefreshTokenToDataStore("")
                                authViewModel.saveIsUserLoggedInToDataStore(false)
                                authViewModel.saveIsOnboardingShownToDataStore(false)
                                /* TODO-FIXME-CLEANUP
                                navController.navigate(Screen.PermissionsScreen.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                                */
                                // Logout
                            }) {
                            Text(
                                text = "Logout",
                                color = Color.White
                            )
                        }
                        /* TODO-FIXME-CLEANUP
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.clickable {
                                // TODO
                            },
                            text = "",
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Bold

                        )
                        */
                        AppVersionInfo()
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            thickness = 1.dp,
                            color = TertiaryColor
                        )
                        Spacer(modifier = Modifier.height(180.dp))
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun TabFiveScreenPreview() {
    TabFiveScreen(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}