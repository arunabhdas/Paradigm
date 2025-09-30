package app.paradigmatic.paradigmaticapp.ui.screens

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.destinations.LoginScreenDestination
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.AuthViewModel
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.RegisterUiState
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.SecondaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import com.ramcosta.composedestinations.annotation.Destination
import app.paradigmatic.paradigmaticapp.ui.screens.composables.IdleUI
import app.paradigmatic.paradigmaticapp.ui.screens.composables.LoadingUI
import app.paradigmatic.paradigmaticapp.ui.theme.TransparentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun RegistrationScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    authViewModel: AuthViewModel = hiltViewModel()
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
    val usernameValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }

    val uiState by authViewModel.registerUiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { contentPadding ->
        // Screen content
        when (uiState) {
            is RegisterUiState.Loading -> {
                // Show loading indicator
                LoadingUI()
            }
            is RegisterUiState.Success -> {

                scope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = "Registration Successful. Please click on link in email to verify",
                            actionLabel = "Proceed to Login",
                            // Defaults to SnackbarDuration.Short
                            duration = SnackbarDuration.Indefinite
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            navigator.navigate(
                                LoginScreenDestination(
                                    isFromRegistration = true
                                )
                            )
                        }
                        SnackbarResult.Dismissed -> {
                            /* Handle snackbar dismissed */
                        }
                    }
                }

                // navigator.navigate(MainScreenBottomNavigationDestination)

            }
            is RegisterUiState.Error -> {

                // Show error message
                // ErrorUI((uiState as LoginUiState.Error).exception)
                scope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = "Sign Up Error. Please try again",
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
            RegisterUiState.Idle -> {
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
                Image(
                    painter = painterResource(id = R.drawable.appicon),
                    contentDescription = stringResource(id = R.string.one_logo_content_description),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(150.dp)
                )
                ClickableText(
                    text = AnnotatedString(stringResource(id = R.string.app_name)),
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        showExpandedText = !showExpandedText
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(visible = showExpandedText) {
                    Text(
                        text = "Powered by Xlr8 Pro",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
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
                        value = usernameValue.value,
                        onValueChange = { usernameValue.value = it },
                        label = { Text(text = "Username", color = Color.White) },
                        placeholder = { Text(text = "Enter username", color = Color.White) },
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

                            // navController.popBackStack()
                            scope.launch {
                                snackbarHostState.showSnackbar("Sign Up in progress")
                            }
                            keyboardController?.hide()
                            authViewModel.registerUser(
                                emailValue.value,
                                usernameValue.value,
                                passwordValue.value
                            )

                            /* TODO-FIXME-CLEANUP
                            navController.navigate(Screen.MainScreen.route) {

                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                            */
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = PrimaryColor
                        ),
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_up),
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

                            }),
                            color = PrimaryColor, fontSize = 14.sp
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
fun RegistrationScreenPreview() {
    // TODO-FIXME-CLEANUP LandingScreen(navigator = MockDestinationsNavigator())
    RegistrationScreen(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )

}
