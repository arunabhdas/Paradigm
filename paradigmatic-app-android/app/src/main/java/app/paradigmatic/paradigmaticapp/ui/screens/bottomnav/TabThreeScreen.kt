package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import android.content.Context
import android.content.Intent
import android.provider.Settings
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.presentation.common.Constants
import app.paradigmatic.paradigmaticapp.presentation.data.model.FetchRoutesResponseItem
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.FetchRoutesUiState
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.RoutesViewModel
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.ui.components.AboutDialog
import app.paradigmatic.paradigmaticapp.ui.screens.CustomScreenBar
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.routetaskmanager.components.StopComponent
import app.paradigmatic.paradigmaticapp.ui.routetaskmanager.components.WelcomeMessageComponent
import app.paradigmatic.paradigmaticapp.ui.screens.composables.TopRightMenuButtonBottomSheetContent
import app.paradigmatic.paradigmaticapp.ui.screens.composables.Xlr8ProActionItem
import app.paradigmatic.paradigmaticapp.ui.theme.TertiaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun TabThreeScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel(),
    routesViewModel: RoutesViewModel = hiltViewModel(),
    onShowDetail: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // State variables
    val userId by authViewModel.userId.collectAsState()
    val username by authViewModel.username.collectAsState()
    val accessToken by authViewModel.accessToken.collectAsState()

    var showAboutDialog = remember { mutableStateOf(false) }
    val permissionState = rememberMultiplePermissionsState(
        PermUtils.permissions
    )


    val routeUiState by routesViewModel.routeUiState.collectAsState()

    // Unit indicates this effect only runs once when the composable enters the composition.
    LaunchedEffect(
        key1 = Unit,
        key2 = username
    ) {
            routesViewModel.fetchRoutes(
                "${Constants.Api.AUTHORIZATION_BEARER} $accessToken",
                userId
            )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.offset(x = 0.dp,  y = -80.dp),
                onClick = {
                    onShowDetail()
                }) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Map"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {paddingValues ->

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
                    RoutesList(
                        navigator = navigator,
                        fetchRoutesResponseList = fetchRoutesResponseList,
                        username = username,
                        onShowDetail = onShowDetail
                    )
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

/**
 * Take the user to location settings to enable location services
 *
 * @param activity
 */
fun enableLocation(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

@Composable
fun LoadingComponent(
) {
    val context = LocalContext.current
    var showAboutDialog = remember { mutableStateOf(false) }
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
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ErrorComponent(
    navigator: DestinationsNavigator,
    message: String,
    username: String
) {
    val context = LocalContext.current
    var showAboutDialog = remember { mutableStateOf(false) }
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
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 128.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    // Add the components that were previously above the LazyColumn here
                    // For example, Image, ClickableText, etc.

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CustomScreenBar(
                            stringResource(R.string.tab_3_name),
                            {
                                PermUtils.openAppSettings(context)
                            },
                            {
                                PermUtils.openAppSettings(context)
                            }
                        )


                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.appicon),
                            contentDescription = stringResource(id = R.string.content_description),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(150.dp)
                        )

                        Divider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            thickness = 1.dp,
                            color = TertiaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // TODO-FIXME-CLEANUP RouteProfileHeader()

                        WelcomeMessageComponent(
                            title = message,
                            subtitle = "",
                            number = 0
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            thickness = 1.dp,
                            color = TertiaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                }

            }
        }

    }
}

@Composable
fun RoutesList(
    navigator: DestinationsNavigator,
    fetchRoutesResponseList: List<FetchRoutesResponseItem>,
    username: String,
    onShowDetail: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showAboutDialog = remember { mutableStateOf(false) }

    var showBottomSheetMenuModal = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showTopRightMenuBottomSheetMenuModal = remember { mutableStateOf(false) }
    val topRightMenuBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val numberOfStops = if (fetchRoutesResponseList.isNotEmpty() && fetchRoutesResponseList[0]?.stops != null) {
        fetchRoutesResponseList[0].stops.size
    } else {
        0
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
                        Text(text = "Info", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider()
                    }
                }
            ) {
                TabThreeScreenBottomSheetContent(
                    navigator = navigator,
                    onItemOneClick = {
                        Timber.d("TabThreeScreen Item One was clicked")
                    },
                    onHideButtonClick = {
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) showBottomSheetMenuModal.value = false
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
                    description = "Tap on route stops for navigation directions",
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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 128.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    // Add the components that were previously above the LazyColumn here
                    // For example, Image, ClickableText, etc.

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CustomScreenBar(
                            stringResource(R.string.tab_3_name),
                            {
                                showAboutDialog.value = true
                            },
                            {
                                // PermUtils.openAppSettings(context)
                                // showBottomSheetMenuModal.value = true
                                showTopRightMenuBottomSheetMenuModal.value = true
                            }
                        )


                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.appicon),
                            contentDescription = stringResource(id = R.string.content_description),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(75.dp)
                        )

                        Divider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            thickness = 1.dp,
                            color = TertiaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // RouteProfileHeader()

                        WelcomeMessageComponent(
                            title = username,
                            subtitle = "",
                            number = numberOfStops
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            thickness = 1.dp,
                            color = TertiaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                }
                items(
                    fetchRoutesResponseList[0].stops
                ) {stop ->
                    StopComponent(
                        stopItem = stop,
                        onClick = {
                            // TODO-FIXME-CLEANUP
                            // navigator.navigate(TabThreeDetailScreenDestination)
                            onShowDetail()
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }

    }
}




@Composable
fun TabThreeScreenBottomSheetContent(
    navigator: DestinationsNavigator,
    onItemOneClick: () -> Unit,
    onHideButtonClick: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            ListItem(
                headlineContent = { Text(text = "Navigate to this location? ") },
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
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onHideButtonClick
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@Preview
@Composable
fun TabThreeScreenPreview() {
   TabThreeScreen(
       navController = rememberNavController(),
       navigator = MockDestinationsNavigator(),
       authViewModel = hiltViewModel(),
       onShowDetail = {}
   )
}

@Composable
fun TabThreeActionItemPreview() {
    MaterialTheme {
        Xlr8ProActionItem(
            imageId = R.drawable.appicon, // Replace with a valid drawable resource ID
            text = "Sample Text",
            contentDescription = "Sample Description"
        ) {
            // TODO-FIXME onClick
        }
    }
}