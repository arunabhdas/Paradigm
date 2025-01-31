package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskStop
import app.paradigmatic.paradigmaticapp.presentation.common.Constants
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.PlacesViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.FetchRoutesUiState
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.GeocodeUiState
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.GeocodingViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.RoutesViewModel
import app.paradigmatic.paradigmaticapp.ui.composemaps.viewmodel.MapsViewModel
import app.paradigmatic.paradigmaticapp.ui.components.AboutDialog
import app.paradigmatic.paradigmaticapp.ui.composemaps.model.MapEvent
import app.paradigmatic.paradigmaticapp.ui.screens.CustomTopAppBar
import app.paradigmatic.paradigmaticapp.ui.screens.composables.RoutePlannerToolBottomSheetContent
import app.paradigmatic.paradigmaticapp.ui.screens.composables.TopRightMenuButtonBottomSheetContent
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.launch
import timber.log.Timber


@Destination
@Composable
fun TabOneScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel(),
    mapsViewModel: MapsViewModel = hiltViewModel(),
    routesViewModel: RoutesViewModel = hiltViewModel(),
    geocodingViewModel: GeocodingViewModel = hiltViewModel(),
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
    // val chargingStations by chargingStationsViewModel.chargingStations.collectAsState()
    val routeUiState by routesViewModel.routeUiState.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                userLocation?.latitude ?: 37.3530731,
                userLocation?.longitude ?: -122.165254
            ),
            12f
        )
    }

    val showNavigateDialog = remember { mutableStateOf(false) }
    val selectedGeoLatLng = remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val selectedTaskStop = remember {
        mutableStateOf(
            TaskStop(
                title = "",
                description = "",
                formattedAddressOrigin = "",
                latOrigin = 0.0,
                lngOrigin = 0.0,
                formattedAddressDestination = "",
                latDestination = 0.0,
                lngDestination = 0.0
            )
        )
    }
    val selectedMarkerLatLng = remember { mutableStateOf(LatLng(0.0, 0.0)) }


    var showBottomSheetMenuModal = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showTopRightMenuBottomSheetMenuModal = remember { mutableStateOf(false) }
    val topRightMenuBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showRoutePlannerToolBottomSheetMenuModal = remember { mutableStateOf(false) }
    val routePlannerToolBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showInfoWindowLongClickBottomSheetMenuModal = remember { mutableStateOf(false) }
    val showInfoWindowLongClickBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )



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
            /* TODO-FIXME-CLEANUP
            routesViewModel.fetchRoutesWithStopsAndLatLng(
                "${Constants.AUTHORIZATION_BEARER} $accessToken",
                userId

            )
            */
        }
    }

    LaunchedEffect(geocodingViewModel.geocodeUiState) {
        geocodingViewModel.geocodeUiState.collect { state ->
            when (state) {
                is GeocodeUiState.Success -> {
                    selectedTaskStop.value.formattedAddressDestination = state.address
                    // Trigger any additional actions you need after updating the address
                    mapsViewModel.onEvent(
                        MapEvent.OnMapLongClick(selectedGeoLatLng.value),
                        selectedTaskStop.value
                    )
                }
                is GeocodeUiState.Error -> {
                    // Handle error case
                    Timber.e("Geocode error: ${state.exception}")
                }
                else -> {} // Handle other states if necessary
            }
        }
    }

    SideEffect(
    ) {
        Timber.d("- TabThreeDetailScreen")
        window.statusBarColor = PrimaryColor.toArgb()
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.offset(x = 0.dp,  y = -80.dp),
                onClick = {
                    Timber.d("------TabOneScreened FAB Clicked")
                    showRoutePlannerToolBottomSheetMenuModal.value = true
                }) {
                /*
                Icon(
                    imageVector = if (mapsViewModel.state.isFalloutMap) {
                        Icons.Default.ToggleOn
                    } else {
                        Icons.Default.ToggleOff
                    },
                    contentDescription = "Toggle Fallout Map"
                )
                */
                val image = if (mapsViewModel.state.isFalloutMap) {
                    painterResource(id = R.drawable.toast_24px) // Update this line
                } else {
                    painterResource(id = R.drawable.toast_24px) // Assuming you have an alternative
                }

                Icon(
                    painter = image,
                    contentDescription = "Toggle Fallout Map"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            CustomTopAppBar(
                headertitle = "Route Planner Tool",
                title = "Dashboard",
                onLeftIconClick = {
                    showAboutDialog.value = true
                },
                onRightIconClick = {
                    showTopRightMenuBottomSheetMenuModal.value = true
                }
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
            // BottomSheetMenuModal
            if (showBottomSheetMenuModal.value) {
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    onDismissRequest = { showBottomSheetMenuModal.value = false },
                    dragHandle = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BottomSheetDefaults.DragHandle()
                            Text(text = "Origin", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider()
                        }
                    }
                ) {
                    TabOneScreenBottomSheetContent(
                        navigator = navigator,
                        onItemOneClick = {
                            Timber.d("TabOneScreen Item One was clicked")
                            selectedGeoLatLng.value.let { latLng ->
                                geocodingViewModel.fetchAddressFromLatLng(latLng.latitude, latLng.longitude)
                            }
                            showBottomSheetMenuModal.value = false

                            /*
                            mapsViewModel.onEvent(
                                MapEvent.OnMapLongClick(selectedGeoLatLng.value)
                            )
                            */

                            /* TODO-FIXME-CLEANUP
                            showNavigateDialog.value = false
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("google.navigation:q=${selectedMarkerLatLng.value.latitude},${selectedMarkerLatLng.value.longitude}")
                            )
                            context.startActivity(intent)
                            */
                        },
                        onHideButtonClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) showBottomSheetMenuModal.value = false
                            }
                        }
                    )
                }
            }
            // InfoWindowLongClickBottomSheetMenuModal
            if (showInfoWindowLongClickBottomSheetMenuModal.value) {
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    onDismissRequest = { showInfoWindowLongClickBottomSheetMenuModal.value = false },
                    dragHandle = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BottomSheetDefaults.DragHandle()
                            Text(text = "Origin", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider()
                        }
                    }
                ) {
                    TabOneScreenInfoWindowLongClickBottomSheetContent(
                        navigator = navigator,
                        onItemOneClick = {
                            Timber.d("TabOneScreen Item One was clicked")
                            mapsViewModel.onEvent(
                                MapEvent.OnInfoWindowLongClick(selectedTaskStop.value),
                                selectedTaskStop.value
                            )
                            /* TODO-FIXME-CLEANUP
                            showNavigateDialog.value = false
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("google.navigation:q=${selectedMarkerLatLng.value.latitude},${selectedMarkerLatLng.value.longitude}")
                            )
                            context.startActivity(intent)
                            */
                        },
                        onHideButtonClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!showInfoWindowLongClickBottomSheetState.isVisible) {
                                    showInfoWindowLongClickBottomSheetMenuModal.value = false
                                }
                            }
                        }
                    )
                }
            }

            // TopRightMenuBottomSheetMenuModal
            if (showTopRightMenuBottomSheetMenuModal.value) {
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    onDismissRequest = { showTopRightMenuBottomSheetMenuModal.value = false },
                    dragHandle = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BottomSheetDefaults.DragHandle()
                            Text(text = "Info", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider()
                        }
                    }
                ) {
                    TopRightMenuButtonBottomSheetContent(
                        navigator = navigator,
                        description = "Long click on map to add task stop marker",
                        onItemOneClick = {
                            Timber.d("TopRightMenuButton Item One was clicked")
                        },
                        onHideButtonClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!topRightMenuBottomSheetState.isVisible) {
                                    showTopRightMenuBottomSheetMenuModal.value = false
                                }
                            }
                        }
                    )
                }
            }

            // RoutePlannerToolBottomSheetMenuModal
            if (showRoutePlannerToolBottomSheetMenuModal.value) {
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    onDismissRequest = { showRoutePlannerToolBottomSheetMenuModal.value = false },
                    dragHandle = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            BottomSheetDefaults.DragHandle()
                            Text(text = "Route Planner Stops", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider()
                        }
                    }
                ) {
                    RoutePlannerToolBottomSheetContent(
                        navigator = navigator,
                        stops = mapsViewModel.state.taskStops,
                        description = "Route Planner Task Stops",
                        onItemOneClick = {
                            Timber.d("RoutePlannerToolFAB Item One was clicked")
                        },
                        onHideButtonClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!routePlannerToolBottomSheetState.isVisible) {
                                    showRoutePlannerToolBottomSheetMenuModal.value = false
                                }
                            }
                        }
                    )
                }
            }

            // Dialog
            // Show dialog when a marker is clicked
            if (showNavigateDialog.value) {
                AlertDialog(
                    onDismissRequest = { showNavigateDialog.value = false },
                    title = { Text("Navigate") },
                    text = { Text("Do you want to navigate to this location?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showNavigateDialog.value = false
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(
                                        "google.navigation:q=${selectedMarkerLatLng.value.latitude},"
                                                + "${selectedMarkerLatLng.value.longitude}")
                                )
                                context.startActivity(intent)
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showNavigateDialog.value = false }) {
                            Text("No")
                        }
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
                                zoomControlsEnabled = true,

                            ),
                            cameraPositionState = cameraPositionState,
                            onMapLongClick = { latLng ->
                                selectedGeoLatLng.value = latLng
                                showBottomSheetMenuModal.value = true
                                /*
                                mapsViewModel.onEvent(
                                    MapEvent.OnMapLongClick(latLng)
                                )
                                */
                            }
                        ) {
                            // Displaying charging stations as markers
                            Timber.d("------${fetchRoutesResponseList[0].stops.size}")
                            mapsViewModel.state.taskStops.forEach { taskStop ->
                                Marker(
                                    state = MarkerState(
                                        position = LatLng(taskStop.latOrigin, taskStop.lngOrigin)
                                    ),
                                    title = "Task Stop ${taskStop.formattedAddressOrigin}",
                                    snippet = "Long click for options${taskStop.formattedAddressDestination}",

                                    /* TODO-FIXME-BRINGBACK-DELETE-MARKER
                                    onInfoWindowLongClick = {
                                        Timber.d("--------OnInfoWindowLongClick")
                                        mapsViewModel.onEvent(
                                            MapEvent.OnInfoWindowLongClick(stop)
                                        )
                                    },
                                    */
                                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                                    onClick = {marker ->
                                        marker.showInfoWindow()
                                        true
                                    },
                                    onInfoWindowLongClick = {marker ->
                                        Timber.d("--------OnInfoWindowLongClick")
                                        // showNavigateDialog.value = true
                                        selectedMarkerLatLng.value = marker.position
                                        selectedTaskStop.value = taskStop
                                        // showBottomSheetMenuModal.value = true
                                        showInfoWindowLongClickBottomSheetMenuModal.value = true
                                        true
                                    },

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

@Composable
fun TabOneScreenBottomSheetContent(
    navigator: DestinationsNavigator,
    onItemOneClick: () -> Unit,
    onHideButtonClick: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            ListItem(
                headlineContent = { Text(text = "Add Origin Marker? ") },
                leadingContent = {
                    Icon(imageVector = Icons.Default.FormatListBulleted, contentDescription = null)
                },
                modifier = Modifier.clickable {
                    // Navigate to TabThreeDetailScreen
                    // navigator.navigate(TabThreeDetailScreenDestination)
                    onItemOneClick()
                }
            )
        }
        /*
        item {
            ListItem(
                headlineContent = { Text(text = "Item ") },
                leadingContent = {
                    Icon(imageVector = Icons.Default.FormatListBulleted, contentDescription = null)
                },
                modifier = Modifier.clickable {
                    onItemTwoClick()
                }
            )
        }
        */
        item {
            androidx.compose.material3.Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onHideButtonClick
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
fun TabOneScreenInfoWindowLongClickBottomSheetContent(
    navigator: DestinationsNavigator,
    onItemOneClick: () -> Unit,
    onHideButtonClick: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            ListItem(
                headlineContent = { Text(text = "Remove Origin Marker? ") },
                leadingContent = {
                    Icon(imageVector = Icons.Default.FormatListBulleted, contentDescription = null)
                },
                modifier = Modifier.clickable {
                    // Navigate to TabThreeDetailScreen
                    // navigator.navigate(TabThreeDetailScreenDestination)
                    onItemOneClick()
                }
            )
        }
        /*
        item {
            ListItem(
                headlineContent = { Text(text = "Item ") },
                leadingContent = {
                    Icon(imageVector = Icons.Default.FormatListBulleted, contentDescription = null)
                },
                modifier = Modifier.clickable {
                    onItemTwoClick()
                }
            )
        }
        */
        item {
            androidx.compose.material3.Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onHideButtonClick
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

/*
@Preview
@Composable
fun TabOneScreenPreview() {
    TabOneScreen(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}
*/