package app.paradigmatic.paradigmaticapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.destinations.MainScreenDestination
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.SecondaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.TransparentColor
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.LoginSupabaseUiState
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.LoginUiState
import app.paradigmatic.paradigmaticapp.ui.screens.composables.ErrorUI
import app.paradigmatic.paradigmaticapp.ui.screens.composables.IdleUI
import app.paradigmatic.paradigmaticapp.ui.screens.composables.LoadingUI
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun LoginScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel(),
    isFromRegistration: Boolean = false,
    isManuallyLoggedOut: Boolean = false
) {
    val context = LocalContext.current
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

    val uiState by authViewModel.loginUiState.collectAsState()
    val supabaseUiState by authViewModel.loginSupabaseUiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle Supabase login state
    LaunchedEffect(supabaseUiState) {
        when (supabaseUiState) {
            is LoginSupabaseUiState.Success -> {
                Toast.makeText(context, "Supabase Login Success", Toast.LENGTH_SHORT).show()
                navigator.navigate(MainScreenDestination)
            }
            is LoginSupabaseUiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = (supabaseUiState as LoginSupabaseUiState.Error).exception,
                        duration = SnackbarDuration.Long
                    )
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(key1 = authViewModel.isUserLoggedIn) {
        if (authViewModel.isUserLoggedIn.value) {
            val emailValue = authViewModel.email.value
            val passwordValue = authViewModel.password.value
            if (isManuallyLoggedOut) {

            } else {
                /* TODO-FIXME-BRINGBACK
                authViewModel.loginUser(
                    emailValue,
                    passwordValue
                )
                */
                authViewModel.loginSupabaseUser(
                    emailValue,
                    passwordValue
                )
            }

        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { contentPadding ->
        // Screen content

        when (uiState) {
            is LoginUiState.Loading -> {
                // Show loading indicator
                LoadingUI()
            }

            is LoginUiState.Success -> {
                // Handle success
                Toast.makeText(
                    context,
                    "Login Success",
                    Toast.LENGTH_SHORT
                ).show()
                // TODO-FIXME-CLEANUP navigator.navigate(MainScreenBottomNavigationDestination)
                navigator.navigate(MainScreenDestination)
            }

            is LoginUiState.Error -> {
                // Show error message
                ErrorUI((uiState as LoginUiState.Error).exception)
                scope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = "Login Error. Please try again",
                            actionLabel = "",
                            // Defaults to SnackbarDuration.Short
                            duration = SnackbarDuration.Indefinite
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {

                        }

                        SnackbarResult.Dismissed -> {
                            /* Handle snackbar dismissed */
                        }
                    }
                }
            }

            LoginUiState.Idle -> {
                // Show initial UI or nothing
                IdleUI()
            }
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
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.onboarding_0),
                    contentDescription = "Image1",
                    modifier = Modifier.padding(start = 50.dp, end = 50.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(visible = showExpandedText) {
                    Text(
                        text = "Ignite Your Potential",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    OutlinedTextField(
                        value = emailValue.value,
                        onValueChange = { emailValue.value = it },
                        label = { Text(text = "Email", color = Color.White) },
                        placeholder = { Text(text = "Enter email address", color = Color.White) },
                        singleLine = true,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedTextColor = Color.White

                        )
                    )

                    OutlinedTextField(
                        value = passwordValue.value,
                        onValueChange = { passwordValue.value = it },
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisibility.value = !passwordVisibility.value
                            }) {
                                Icon(
                                    painter = passwordVector, contentDescription = "Password icon",
                                    tint = if (passwordVisibility.value) PrimaryColor else Color.Gray
                                )
                            }
                        },
                        label = { Text(text = "Password", color = Color.White) },
                        placeholder = { Text(text = "Enter the password", color = Color.White) },
                        singleLine = true,
                        visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.padding(10.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Login in progress")
                            }
                            keyboardController?.hide()
                            // TODO-FIXME-BRINGBACK authViewModel.loginUser(emailValue.value, passwordValue.value)
                            authViewModel.loginSupabaseUser(emailValue.value, passwordValue.value)
                            // TODO-FIXME-CLEANUP navigator.navigate(MainScreenBottomNavigationDestination)

                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = PrimaryColor
                        ),
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .wrapContentHeight(), contentAlignment = Alignment.CenterEnd
                    ) {
                        /* TODO-FIXME-CLEANUP-FORGOT-MY-PASSWORD
                        Text(
                            text = "Forgot my password",
                            modifier = Modifier.clickable(onClick = {
                            }), color = YellowGreen, fontSize = 14.sp
                        )
                        */
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    /* TODO-FIXME-CLEANUP-CREATE-AN-ACCOUNT
                    Text(
                        text = "Create an account",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate(Screen.MainScreen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }

                        })
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    */
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
                            backgroundColor = PrimaryColor
                        ),
                    ) {
                        Text(
                            text = "Back",
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
    }

}


@Composable
@Preview
fun LoginScreenPreview() {
    // TODO-FIXME-CLEANUP LandingScreen(navigator = MockDestinationsNavigator())
    LoginScreen(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}
