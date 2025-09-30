package app.paradigmatic.paradigmaticapp.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import app.paradigmatic.paradigmaticapp.viewmodel.MockMainViewModel
import app.paradigmatic.paradigmaticapp.viewmodel.MainViewModel
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.ui.components.BluetoothScanPermissionTextProvider
import app.paradigmatic.paradigmaticapp.ui.components.CameraPermissionTextProvider
import app.paradigmatic.paradigmaticapp.ui.components.CoarseLocationPermissionTextProvider
import app.paradigmatic.paradigmaticapp.ui.components.FineLocationPermissionTextProvider
import app.paradigmatic.paradigmaticapp.ui.components.InfoDialog
import app.paradigmatic.paradigmaticapp.ui.components.PermissionsInfoDialog
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.navigation.Screen
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.TransparentColor
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber

@Destination
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsScreen(
    // TODO-DEPRECATE navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val permissionsToRequest = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    val osVersion = Build.VERSION.SDK_INT

    /* TODO-FIXME-CLEANUP
    if (osVersion >= Build.VERSION_CODES.S_V2) {
        // Android 14 (API level 33)
        permissionsToRequest.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
    }
    */

    /* TODO-FIXME-CLEANUP
    val permissionState = rememberMultiplePermissionsState(
        PermUtils.permissions
    )
    */

    // Use LocalLifecycleOwner to observe lifecycle changes
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = remember { lifecycleOwner.lifecycle }
    val permissionState = rememberMultiplePermissionsState(permissionsToRequest)

    val dialogQueue = mainViewModel.visiblePermissionDialogQueue
    var showInfoDialog = remember { mutableStateOf(false) }
    var showPermissionsInfoDialogFlow = remember { mutableStateOf(true) }

    val coarseLocationResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted ->
            mainViewModel.onPermissionResult(
                permission = Manifest.permission.ACCESS_COARSE_LOCATION,
                isGranted = isGranted
            )
        }
    )

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach {permission ->
                mainViewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }

        }
    )

    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionsInfoDialog(
                title = "Permission Required",
                description = permission.toString(),
                permissionTextProvider = when (permission) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        CoarseLocationPermissionTextProvider()
                    }
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        FineLocationPermissionTextProvider()
                    }
                    /* TODO-FIXME-ACCESS_BACKGROUND_LOCATION
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                       BackgroundLocationPermissionTextProvider()
                    }
                    */
                    Manifest.permission.CAMERA -> {
                        CameraPermissionTextProvider()
                    }
                    Manifest.permission.BLUETOOTH_SCAN -> {
                        BluetoothScanPermissionTextProvider()
                    }
                    /* TODO-FIXME-CLEANUP
                    Manifest.permission.FOREGROUND_SERVICE_LOCATION -> {
                        ForegroundServicePermissionTextProvider()
                    }
                    */
                    else -> return@forEach
                },
                isPermanentlyDeclined = permissionState.shouldShowRationale,
                navigator = navigator,
                onDismiss =  {
                    mainViewModel::dismissDialog
                },
                onBottomButtonTapped = {
                    // TODO-FIXME-CLEANUP mainViewModel.dismissDialog()
                    Timber.d("----------Tapped on Enable Location button")
                    PermUtils.openAppSettings(context = context)
                }
            )
        }

    val isOnboardingShown by authViewModel.isOnboardingShown.collectAsState()

    // Observe lifecycle to detect when the app returns to the foreground
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Check if permissions were granted when returning to the app
                if (permissionState.allPermissionsGranted) {
                    // Dismiss dialog or perform necessary action
                    mainViewModel.dismissDialog()
                }
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = authViewModel.isOnboardingShown) {
        Timber.d("--------PermissionsScreen - Is Onboarding Shown:  + ${authViewModel.isOnboardingShown}")
    }



    LaunchedEffect(
        key1 = permissionState.allPermissionsGranted,
        key2 = isOnboardingShown
    ) {
        showInfoDialog.value = true
        if (permissionState.allPermissionsGranted) {
            Timber.d("- Permissions were granted")
            Timber.d("- Starting Service")

        }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(horizontal = 0.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.onboarding_0),
                contentDescription = "Image1",
                modifier = Modifier.padding(start = 50.dp, end = 50.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.app_onboarding_description)),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    showExpandedText = !showExpandedText
                }
            )
            /*
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (!showInfoDialog.value) {
                        navigator.navigate(LandingScreenMainDestination())
                    } else {
                        showInfoDialog.value = true
                        showPermissionsInfoDialogFlow.value = false
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PrimaryColor
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp))
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Text(text = if (!showInfoDialog.value) "Continue" else "Request Permissions",
                    color = Color.White
                )
            }
            */
            Button(
                onClick = {
                    navigator.navigate(
                        Screen.LoginScreen.route
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PrimaryColor
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
            ) {
                Text(
                    text = "Login",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    navigator.navigate(
                        Screen.RegistrationScreen.route
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PrimaryColor
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White
                )
            }
            if (showInfoDialog.value) {
                InfoDialog(
                    title = stringResource(R.string.location_permission_title),
                    description = stringResource(R.string.location_permission_desc),
                    navigator = navigator,
                    onDismiss = {

                    }
                ) {
                    showInfoDialog.value = false
                    showPermissionsInfoDialogFlow.value = true
                    multiplePermissionResultLauncher.launch(
                        permissionsToRequest.toTypedArray()
                    )
                }
            }
        }
    }
}



@Composable
fun PermissionsScreenMainPreview() {
    PermissionsScreen(
        mainViewModel = MockMainViewModel(),
        navigator = MockDestinationsNavigator(),
        authViewModel = hiltViewModel()
    )
}