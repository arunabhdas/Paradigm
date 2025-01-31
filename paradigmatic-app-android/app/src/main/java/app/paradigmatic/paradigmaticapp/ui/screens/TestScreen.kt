package app.paradigmatic.paradigmaticapp.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.data.gattble.model.ConnectionState
import app.paradigmatic.paradigmaticapp.viewmodel.MainViewModel
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.ui.navigation.Screen
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.SecondaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.TransparentColor
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun TestScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController
    // TODO-FIXME navigator: DestinationsNavigator
) {
    val gradientColors = listOf(
        PrimaryColor,
        SecondaryColor
    )
    var showExpandedText by remember {
        mutableStateOf(false)
    }
    val passwordVector = painterResource(id = R.drawable.password_eye)
    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }

    val ctx = LocalContext.current
    val permissionState = rememberMultiplePermissionsState(
        PermUtils.permissionsList
    )

    val lifeCycleOwner = LocalLifecycleOwner.current

    val bleConnectionState = mainViewModel.connectionState

    var bleScanner: BluetoothLeScanner? = null

    DisposableEffect(
        key1 = lifeCycleOwner,
        effect = {
            val observer = LifecycleEventObserver{_, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchMultiplePermissionRequest(

                    )

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
        if (permissionState.allPermissionsGranted) {
            if (bleConnectionState == ConnectionState.Uninitialized) {
                mainViewModel.initializeConnection()
                Toast.makeText(
                    ctx,
                    "- Initializing Scan",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }



    LaunchedEffect(key1 = "LandingScreenAppeared") {
        Timber.d( "route: ${Screen.LandingScreen.route}")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = gradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.appicon),
                contentDescription = stringResource(id = R.string.one_logo_content_description),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp)
            )
            ClickableText(
                text = AnnotatedString("Test Scan BLE"),
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
                    text = "Powered by Xlr8Pro",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {

                    }, modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary // Updated color scheme for Material 3
                    )
                ) {
                    Text(text = "Scan BLE", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.padding(20.dp))

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        /* TODO-FIXME
                    navigator.navigate(
                    )
                    */
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(TransparentColor),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary // Updated color scheme for Material 3
                    )
                ) {
                    Text(text = "Back")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }

}

@Composable
@Preview
fun TestScreenPreview() {
    // TODO-FIXME-CLEANUP LandingScreen(navigator = MockDestinationsNavigator())
    TestScreen(navController = rememberNavController())
}
