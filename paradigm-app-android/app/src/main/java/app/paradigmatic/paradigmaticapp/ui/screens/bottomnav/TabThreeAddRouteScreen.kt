package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.presentation.common.Constants
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.PlacesViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.FetchRoutesUiState
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.RoutesViewModel
import app.paradigmatic.paradigmaticapp.ui.composemaps.viewmodel.MapsViewModel
import app.paradigmatic.paradigmaticapp.ui.components.AboutDialog
import app.paradigmatic.paradigmaticapp.ui.screens.CustomTopAppBar
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber


@Destination
@Composable
fun TabThreeAddRouteScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel(),
    mapsViewModel: MapsViewModel = hiltViewModel(),
    routesViewModel: RoutesViewModel = hiltViewModel(),
    chargingStationsViewModel: PlacesViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val window = (context as Activity).window
    val scope = rememberCoroutineScope()
    // State variables
    val userId by authViewModel.userId.collectAsState()
    val username by authViewModel.username.collectAsState()
    val accessToken by authViewModel.accessToken.collectAsState()
    var showAboutDialog = remember { mutableStateOf(false) }

    // Set properties using MapProperties
    val scaffoldState = rememberScaffoldState()
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    val userLocation by mapsViewModel.userLocation.collectAsState()
    val chargingStations by chargingStationsViewModel.chargingStations.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                userLocation?.latitude ?: 37.3530731,
                userLocation?.longitude ?: -122.165254
            ),
            12f
        )
    }
    val routeUiState by routesViewModel.routeUiState.collectAsState()
    LaunchedEffect(key1 = userLocation) { // Unit indicates this effect only runs once when the composable enters the composition.
        routesViewModel.fetchRoutesWithStopsAndLatLng(
            "${Constants.Api.AUTHORIZATION_BEARER} $accessToken",
            userId

        )

        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    userLocation?.latitude ?: 37.3530731,
                    userLocation?.longitude ?: -122.165254
                ), 12f
            )
        )
    }

    /* TODO-FIXME-CLEANUP
    LaunchedEffect(key1 = userLocation) {
        userLocation?.let { loc->
            val locationString = "${loc.latitude},${loc.longitude}"
            Timber.d("- locationString - $locationString")
            chargingStationsViewModel.fetchChargingStations(ChargingStationsViewModel.DEFAULT_RADIUS, locationString)
        }

        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    userLocation?.latitude ?: 37.3530731,
                    userLocation?.longitude ?: -122.165254
                ), 12f
            )
        )
    }
    */
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            // Camera has stopped moving; fetch new charging stations based on the new center
            val newCenter = cameraPositionState.position.target
            val locationString = "${newCenter.latitude},${newCenter.longitude}"
            chargingStationsViewModel.fetchChargingStations(PlacesViewModel.DEFAULT_RADIUS, locationString)
        }
    }

    SideEffect(
    ) {
        Timber.d("- TabTwoScreen")
        window.statusBarColor = PrimaryColor.toArgb()
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.offset(x = 0.dp,  y = -80.dp),
                onClick = {
                    navigator.navigateUp()
                }) {
                Icon(
                    imageVector = if (mapsViewModel.state.isFalloutMap) {
                        Icons.Default.ToggleOn
                    } else {
                        Icons.Default.ToggleOff
                    },
                    contentDescription = "Toggle Fallout Map"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            CustomTopAppBar(
                headertitle = "Map View",
                title = "Dashboard",
                onLeftIconClick = {
                    showAboutDialog.value = true
                },
                onRightIconClick = { /* Handle action click */ }
            )
        }
    ) { paddingValues ->

        val adjustedPaddingValues = paddingValues.calculateBottomPadding()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = adjustedPaddingValues)
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
            when (routeUiState) {
                is FetchRoutesUiState.Idle -> {

                } // Handle idle state if needed
                is FetchRoutesUiState.Loading -> {
                    // CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    LoadingComponent()
                }
                is FetchRoutesUiState.Success -> {
                    val fetchRoutesResponseList = (routeUiState as FetchRoutesUiState.Success).fetchRoutesResponseList
                    if (fetchRoutesResponseList.isEmpty()) {
                        // Display an empty message or custom UI component
                        ErrorComponent(
                            navigator = navigator,
                            message = "No routes available",
                            username =  username
                        )
                    } else {
                        // Start of Map
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            properties = mapsViewModel.state.properties,
                            uiSettings = MapUiSettings(
                                myLocationButtonEnabled = true,
                                zoomControlsEnabled = false
                            ),
                            cameraPositionState = cameraPositionState,
                            onMapLongClick = {

                            }
                        ) {
                            // Displaying charging stations as markers
                            fetchRoutesResponseList[0].stops.forEach { stop ->
                                val position = LatLng(
                                        stop.destinationAddressLat ?: 0.0,
                                        stop.destinationAddressLng ?: 0.0
                                    )
                                Marker(
                                    state = MarkerState(position = position),
                                    title = stop.title,
                                    snippet = "${stop.orderingNumber} : ${stop.description} (${stop.destinationAddressFull})",
                                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE), // Customization option
                                    onClick = {
                                        // Handle marker click event
                                        // Return true to indicate that the click event is consumed and no further processing (like showing info window) is needed.
                                        false
                                    }
                                )
                            }
                        }
                    }


                }
                is FetchRoutesUiState.Error -> {
                    ErrorComponent(
                        navigator = navigator,
                        message = (routeUiState as FetchRoutesUiState.Error).exception,
                        username =  username
                    )
                    /*
                    Text(
                        text = (routeUiState as FetchRoutesUiState.Error).exception,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                    */
                }
            }



        }
    }

}
/*
@Preview
@Composable
fun TabThreeAddRouteScreenPreview() {
    TabThreeAddRouteScreen(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}
*/