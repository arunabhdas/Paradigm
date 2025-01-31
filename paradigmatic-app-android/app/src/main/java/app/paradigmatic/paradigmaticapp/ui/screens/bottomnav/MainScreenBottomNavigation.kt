package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import app.paradigmatic.paradigmaticapp.ui.zonecomposemaps.viewmodel.ZoneMapViewModel
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.TertiaryColor
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import app.paradigmatic.paradigmaticapp.ui.composemaps.viewmodel.MapsViewModel
import app.paradigmatic.paradigmaticapp.viewmodel.SharedNavigationViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.google.android.gms.location.FusedLocationProviderClient
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MainScreenBottomNavigation(
    navController: NavController,
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel(),
    mapsViewModel: MapsViewModel = hiltViewModel(),
    sharedNavigationViewModel: SharedNavigationViewModel = viewModel()
) {
    var navigationBarItems = remember { MainScreenBottomNavigationBarItems.values() }
    var selectedIndex by remember { mutableStateOf(2) }
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val viewModel: ZoneMapViewModel = viewModel()
    Scaffold(
        modifier = Modifier.padding(all = 0.dp),
        bottomBar = {
            AnimatedNavigationBar(
                selectedIndex = selectedIndex,
                modifier = Modifier
                    .height(128.dp)
                    .navigationBarsPadding(),
                cornerRadius = shapeCornerRadius(cornerRadius = 4.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = PrimaryColor,
                ballColor = MaterialTheme.colorScheme.primary
            ) {
                navigationBarItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable { selectedIndex = item.ordinal },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(26.dp)
                                .padding(bottom = 5.dp),
                            painter = painterResource(id = item.icon),
                            contentDescription = "Bottom Bar Icon",
                            tint = if (selectedIndex == item.ordinal)
                                MaterialTheme.colorScheme.onBackground
                            else
                                TertiaryColor
                        )
                    }
                }
            }
        }
    ) { padding ->
        // Main content

        // Switching content based on selectedIndex
        when (selectedIndex) {
            0 -> TabOneScreen(
                navController = navController,
                navigator = navigator
            )
            1 -> TabTwoScreen(
                navController = navController,
                navigator = navigator,
                state = mapsViewModel.state,
                mapsViewModel = mapsViewModel
                // TODO-FIXME-CLEANUP-BELOW
                // state = viewModel.state.value,
                // setupClusterManager = viewModel::setupClusterManager,
                // calculateZoneViewCenter = viewModel::calculateZoneLatLngBounds
            )
            2 -> {
                // TabThreeScreen visibility
                AnimatedVisibility(
                    visible = !sharedNavigationViewModel.showDetailScreen,
                    enter = slideInHorizontally(
                        // Start entering from the left by specifying full width offset
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(durationMillis = 400)
                    ),
                    exit = slideOutHorizontally(
                        // Exit towards the right by specifying full width offset
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 400)
                    )
                ) {
                    TabThreeScreen(
                        navController = navController,
                        navigator = navigator,
                        onShowDetail = {
                            sharedNavigationViewModel.toggleDetailScreen(true)
                        }
                    )
                }

                // TabThreeScreenDetail visibility
                AnimatedVisibility(
                    visible = sharedNavigationViewModel.showDetailScreen,
                    enter = slideInHorizontally(
                        // Enter from the right
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(durationMillis = 400)
                    ),
                    exit = slideOutHorizontally(
                        // Exit to the left
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(durationMillis = 400)
                    )
                ) {
                    TabThreeDetailScreen(
                        navController = navController,
                        navigator = navigator
                    )
                }
            }
            3 -> TabFourScreen(
                navController = navController,
                navigator = navigator,
                state = mapsViewModel.state,
                mapsViewModel = mapsViewModel
            )
            4 -> TabFiveScreen(
                navController = navController,
                        navigator = navigator
            )
            else -> Text("Screen not found")
        }

    }
}


fun Modifier.noRippleClickable(
    onClick: () -> Unit
) = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

@Preview
@Composable
fun MainScreenBottomNavigationPreview() {
    MainScreenBottomNavigation(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}



