package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.ui.zonecomposemaps.clusters.ZoneClusterManager
import app.paradigmatic.paradigmaticapp.ui.zonecomposemaps.model.ZoneMapState
import app.paradigmatic.paradigmaticapp.ui.components.AboutDialog
import app.paradigmatic.paradigmaticapp.ui.screens.CustomTopAppBar
import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun TabTwoBakScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    state: ZoneMapState,
    setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    calculateZoneViewCenter: () -> LatLngBounds,
) {
    val context = LocalContext.current
    var showAboutDialog = remember { mutableStateOf(false) }
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // Set properties using MapProperties
    val mapProperties = MapProperties(
        isMyLocationEnabled = state.lastKnownLocation != null,
    )
    val cameraPositionState = rememberCameraPositionState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                headertitle = "Telematics",
                title = "Loadboard",
                onLeftIconClick = { /* Handle navigation icon click */ },
                onRightIconClick = { /* Handle action click */ }
            )
        }
    ) { paddingValues ->
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
            // The rest of your existing code goes here
            val context = LocalContext.current
            var showAboutDialog = remember { mutableStateOf(false) }
            lateinit var fusedLocationProviderClient: FusedLocationProviderClient
            // Set properties using MapProperties which you can use to recompose the map
            val mapProperties = MapProperties(
                // Only enable if user has accepted location permissions.
                isMyLocationEnabled = state.lastKnownLocation != null,
            )
            val cameraPositionState = rememberCameraPositionState()
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

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    properties = mapProperties,
                    cameraPositionState = cameraPositionState
                ) {
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    MapEffect(state.clusterItems) { map ->
                        if (state.clusterItems.isNotEmpty()) {
                            val clusterManager = setupClusterManager(context, map)
                            map.setOnCameraIdleListener(clusterManager)
                            map.setOnMarkerClickListener(clusterManager)
                            state.clusterItems.forEach { clusterItem ->
                                map.addPolygon(clusterItem.polygonOptions)
                            }
                            map.setOnMapLoadedCallback {
                                if (state.clusterItems.isNotEmpty()) {
                                    scope.launch {
                                        cameraPositionState.animate(
                                            update = CameraUpdateFactory.newLatLngBounds(
                                                calculateZoneViewCenter(),
                                                0
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // NOTE: Some features of the MarkerInfoWindow don't work currently. See docs:
                    // https://github.com/googlemaps/android-maps-compose#obtaining-access-to-the-raw-googlemap-experimental
                    // So you can use clusters as an alternative to markers.
                    MarkerInfoWindow(
                        state = rememberMarkerState(position = LatLng(49.1, -122.5)),
                        snippet = "Some stuff",
                        onClick = {
                            // This won't work :(
                            System.out.println("Mitchs_: Cannot be clicked")
                            true
                        },
                        draggable = true
                    )
                }
            }
        }
    }

}
/*
@Preview
@Composable
fun TabTwoBakScreenPreview() {
    TabTwoBakScreen(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}
*/