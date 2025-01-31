package app.paradigmatic.paradigmaticapp.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.destinations.LoginScreenDestination
import app.paradigmatic.paradigmaticapp.viewmodel.MockMainViewModel
import app.paradigmatic.paradigmaticapp.viewmodel.MainViewModel
import app.paradigmatic.paradigmaticapp.destinations.PermissionsScreenDestination
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


@ExperimentalPagerApi
@Destination
@RootNavGraph(start = true)
@Composable
fun OnboardingScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel()
    
) {
    val context = LocalContext.current
    val items = OnBoardingItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()

    val isOnboardingShown by authViewModel.isOnboardingShown.collectAsState()
    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()

    LaunchedEffect(key1 = true) {
        Timber.d("-------- OnboardingScreen")
        if (isUserLoggedIn) {
            Timber.d("-------- OnboardingScreen:  + ${isUserLoggedIn}")
            navigator.navigate(LoginScreenDestination())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection(
                onBackClick = {
                    Timber.d("onBackClick")
                    if (pageState.currentPage + 1 > 1) scope.launch {
                        pageState.scrollToPage(pageState.currentPage - 1)
                    }
                },
                onSkipClick = {
                    Timber.d("onSkipClick")
                    if (pageState.currentPage + 1 < items.size) scope.launch {
                        pageState.scrollToPage(items.size - 1)
                    }
                }
            )

            HorizontalPager(
                count = items.size,
                state = pageState,
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth()
            ) { page ->
                OnBoardingItem(items = items[page])
            }
            BottomSection(
                size = items.size,
                index = pageState.currentPage,
                navigator = navigator,
                authViewModel = authViewModel
            ) {
                if (pageState.currentPage + 1 < items.size) scope.launch {
                    pageState.scrollToPage(pageState.currentPage + 1)
                } else {
                    Toast.makeText(
                        context,
                        "Next tapped",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
    }

}

@ExperimentalPagerApi
@Composable
fun TopSection(
    onBackClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(bottom = 12.dp)
            .padding(start = 12.dp)
            .padding(end = 12.dp)

    ) {
        // Back button
        IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(imageVector = Icons.Outlined.KeyboardArrowLeft, contentDescription = null)
        }

        // Skip Button
        TextButton(
            onClick = onSkipClick,
            modifier = Modifier.align(Alignment.CenterEnd),
            contentPadding = PaddingValues(24.dp)
        ) {
            Text(text = "Skip", color = Color.White)
        }
    }
}

@Composable
fun BottomSection(
    size: Int,
    index: Int,
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel,
    onButtonClick: () -> Unit = {}
) {
    val insets = WindowInsets.navigationBars
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(
                bottom = 12.dp,
                top = 12.dp,
                start = 12.dp,
                end = 12.dp
            )

    ) {
        // Indicators
        Indicators(size, index)

        LargeFloatingActionButton(
            onClick = {
                scope.launch {
                    try {
                        authViewModel.saveIsOnboardingShownToDataStore(true)
                        val isUserLoggedIn = authViewModel.isUserLoggedIn.first()
                        val emailValue = authViewModel.email.value
                        val passwordValue = authViewModel.password.value
                        if (isUserLoggedIn) {
                            navigator.navigate(LoginScreenDestination())
                        } else {
                            navigator.navigate(PermissionsScreenDestination())
                        }
                        Timber.d("--------OnboardingScreen - IsUserLogged:  + $isUserLoggedIn")
                        Timber.d("--------OnboardingScreen - username :  + ${authViewModel.username.value}")
                        Timber.d("--------OnboardingScreen - email :  + ${authViewModel.email.value}")
                        Timber.d("--------OnboardingScreen - password :  + ${authViewModel.password.value}")
                        Timber.d("--------OnboardingScreen - Refresh Token:  + ${authViewModel.refreshToken.value}")
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to save IsOnboardingShown")
                    }
                }

                navigator.navigate(PermissionsScreenDestination())
                Toast.makeText(
                    context,
                    "Loading",
                    Toast.LENGTH_LONG
                ).show()
            },
            containerColor = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            Icon(Icons.Outlined.KeyboardArrowRight,
                tint = Color.White,
                contentDescription = "Localized description")
        }
    }
}

@Composable
fun BoxScope.Indicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.align(Alignment.CenterStart)
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0XFFF8E2E7)
            )
    ) {

    }
}
class OnBoardingItems(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object{
        fun getData(): List<OnBoardingItems>{
            return listOf(
                OnBoardingItems(R.drawable.onboarding_0, R.string.onBoardingTitle1, R.string.onBoardingText1),
                OnBoardingItems(R.drawable.onboarding_0, R.string.onBoardingTitle2, R.string.onBoardingText2),
                OnBoardingItems(R.drawable.onboarding_0, R.string.onBoardingTitle3, R.string.onBoardingText3),
                OnBoardingItems(R.drawable.onboarding_0, R.string.onBoardingTitle4, R.string.onBoardingText4),
                OnBoardingItems(R.drawable.onboarding_0, R.string.onBoardingTitle5, R.string.onBoardingText5)
            )
        }
    }
}


@Composable
fun OnBoardingItem(items: OnBoardingItems) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = items.image),
            contentDescription = "Image1",
            modifier = Modifier.padding(start = 50.dp, end = 50.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = stringResource(id = items.title),
            style = MaterialTheme.typography.headlineMedium,
            // fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = items.desc),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp),
            letterSpacing = 1.sp,
        )
    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
@Preview
fun OnboardingScreenPreview() {
    OnboardingScreen(
        mainViewModel = MockMainViewModel(),
        navigator = MockDestinationsNavigator()
    )
}
