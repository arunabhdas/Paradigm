package app.paradigmatic.paradigmaticapp.ui.screens

import android.Manifest
import android.bluetooth.le.BluetoothLeScanner
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.data.gattble.model.ConnectionState
import app.paradigmatic.paradigmaticapp.viewmodel.MockMainViewModel
import app.paradigmatic.paradigmaticapp.viewmodel.MainViewModel
import app.paradigmatic.paradigmaticapp.destinations.MainScreenBottomNavigationDestination
import app.paradigmatic.paradigmaticapp.destinations.MainScreenDestination
import app.paradigmatic.paradigmaticapp.destinations.ScannerScreenDestination
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.permissions.RequestMultiplePermissions
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.navigation.Screen
import app.paradigmatic.paradigmaticapp.ui.theme.TertiaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.TransparentColor
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import timber.log.Timber

@Destination
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LandingScreen(
    // TODO-DEPRECATE navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val permissionState = rememberMultiplePermissionsState(
        PermUtils.permissions
    )

    val lifeCycleOwner = LocalLifecycleOwner.current

    val bleConnectionState = mainViewModel.connectionState

    var bleScanner: BluetoothLeScanner? = null

    DisposableEffect(
        key1 = lifeCycleOwner,
        effect = {
            val observer = LifecycleEventObserver{_, event ->
                if (event == Lifecycle.Event.ON_START) {

                    if (
                        permissionState.allPermissionsGranted &&
                        bleConnectionState == ConnectionState.Disconnected
                        ) {


                        mainViewModel.reconnect()
                    }
                }
                if (event == Lifecycle.Event.ON_STOP) {
                    if (bleConnectionState == ConnectionState.Connected) {
                        mainViewModel.disconnect()
                    }
                }
            }

            lifeCycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifeCycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    LaunchedEffect(
        key1 = permissionState.allPermissionsGranted,
    ) {
        permissionState.launchMultiplePermissionRequest()
        if (permissionState.allPermissionsGranted) {
            Timber.d("- Permissions were granted")
            Timber.d("- Starting Service")
            // Start Service
            // TODO-FIXME-SERVICE
            /*
            Intent(context, DefaultService::class.java).also {
                it.action = DefaultService.Actions.START.toString()
                context.startService(it)
            }
            */
            if (bleConnectionState == ConnectionState.Uninitialized) {
                // TODO-FIXME-IMPROVE
                /* TODO-FIXME
                xlr8ProBeaconViewModel.initializeConnection()
                */
                Toast.makeText(
                    context,
                    "Checking Permissions",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }



    LaunchedEffect(key1 = "LandingScreenAppeared") {
        Timber.d( "route: ${Screen.LandingScreen.route}")
    }
    var showExpandedText by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp)
        ) {
            if (bleConnectionState == ConnectionState.CurrentlyInitializing) {
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp),
                        color = TertiaryColor
                    )
                    if (mainViewModel.initializingMessage != null) {
                        Text(
                            text = mainViewModel.initializingMessage!!,
                            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                        )
                    }
                }
            } else if (!permissionState.allPermissionsGranted) {
                Spacer(modifier = Modifier.height(30.dp))
            } else if (mainViewModel.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = mainViewModel.errorMessage!!
                    )
                    Button(
                        onClick = {
                            Timber.d( "- Debug Started")
                            if (permissionState.allPermissionsGranted) {
                                mainViewModel.initializeConnection()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .background(TransparentColor),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(text = "Initialize")
                    }
                }
            } else if (bleConnectionState == ConnectionState.Connected) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Proximity: ${mainViewModel.proximity}",
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                    )
                    Text(
                        text = "RSSI : ${mainViewModel.rssi}",
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                    )
                }

            }
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = R.drawable.appicon),
                contentDescription = stringResource(id = R.string.content_description),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp)
            )
            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.app_name)),
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    showExpandedText = !showExpandedText
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(visible = showExpandedText) {
                Text(
                    text = "Powered by App Liaison Inc.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {

                    navigator.navigate(
                        MainScreenDestination()
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary // Updated color scheme for Material 3
                )
            ) {
                Text(text = "LandingScreen - Get Started")
            }
            Button(
                onClick = {

                    navigator.navigate(
                        MainScreenBottomNavigationDestination()
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary // Updated color scheme for Material 3
                )
            ) {
                Text(text = "Onboarding")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                          navigator.navigate(
                              Screen.LoginScreen.route
                          )
                          /* TODO-FIXME-DEPRECATE
                          navController.navigate(
                              Screen.LoginScreen.route
                          )
                          */
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary // Updated color scheme for Material 3
                )
            ) {
                Text(text = "Enroll")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                     navigator.navigate(
                         Screen.RegistrationScreen.route
                     )
                    /* TODO-FIXME-DEPRECATE
                    navController.navigate(
                        Screen.RegistrationScreen.route
                    )
                    */
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = "Contacts")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    Timber.d( "- Beacon Scan Started")
                    /* TODO-FIXME-DEPRECATE
                    navigator.navigate(
                        Screen.DebugScreen.route
                    )
                    */
                    mainViewModel.initializeConnection()

                    Toast.makeText(
                        context,
                        "Start Scan",
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = "Start Scan")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    Timber.d( "- Beacon Scan Started")

                    Toast.makeText(
                        context,
                        "Debug Initialized",
                        Toast.LENGTH_LONG
                    ).show()

                    navigator.navigate(
                        ScannerScreenDestination
                    )


                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary // Updated color scheme for Material 3
                )
            ) {
                Text(text = "Debug")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    Timber.d( "- Beacon Scan Started")

                    Toast.makeText(
                        context,
                        "Test Initializing",
                        Toast.LENGTH_LONG
                    ).show()
                    /* TODO-FIXME-BRING-BACK
                    navigator.navigate(
                        Screen.TestScreen.route
                    )
                    */
                    navigator.navigate(
                        Screen.ConnectScreen.route
                    )


                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = "Connect")
            }
            /* TODO-FIXME-CLEANUP
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please go to the app settings and allow the missing permissions",
                style = MaterialTheme.typography.body1.copy(color = Color.White),
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
            */
            RequestMultiplePermissions(
                permissions = listOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.FOREGROUND_SERVICE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )

        }
    }

}



@Composable
@Preview
fun LandingScreenPreview() {
    LandingScreen(
        mainViewModel = MockMainViewModel(),
        navigator = MockDestinationsNavigator()
    )
}