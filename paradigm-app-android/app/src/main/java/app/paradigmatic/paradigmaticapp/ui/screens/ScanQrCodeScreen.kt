package app.paradigmatic.paradigmaticapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.permissions.PermUtils
import app.paradigmatic.paradigmaticapp.ui.components.AboutDialog
import app.paradigmatic.paradigmaticapp.ui.qrcodescanner.QrCodeAnalyzer
import app.paradigmatic.paradigmaticapp.ui.theme.TertiaryColor
import androidx.compose.ui.graphics.Color
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber

@Destination
@Composable
fun ScanQrCodeScreen(
    navController: NavController,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var code by remember { mutableStateOf("") }
    var showAboutDialog = remember { mutableStateOf(false) }
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
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
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Adjust top padding as needed
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CustomScreenBar(
                        stringResource(R.string.tab_1_name),
                        {
                            showAboutDialog.value = true
                        },
                        {
                            PermUtils.openAppSettings(context)
                        }
                    )
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    )
                    {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            thickness = 1.dp,
                            color = TertiaryColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.appicon),
                            contentDescription = stringResource(id = R.string.content_description),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth() // Ensures the Row takes up the full width of its parent
                                .padding(32.dp), // Adds padding around the Row
                            horizontalArrangement = Arrangement.Center, // Centers the contents of the Row horizontally
                            verticalAlignment = Alignment.CenterVertically // Centers the contents of the Row vertically
                        ) {
                            Text(
                                text = code,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            thickness = 1.dp,
                            color = TertiaryColor
                        )

                        if (hasCameraPermission) {
                            CameraPreview(onQrCodeScanned = {qrCode ->
                                code = qrCode
                                Timber.d("----------${code}")
                            })
                        }


                    }
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    onQrCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context)}
    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(context), QrCodeAnalyzer(onQrCodeScanned))
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
            } catch (e: Exception) {
                Timber.e("Use case binding failed", e)
            }

            previewView
        },
        modifier = Modifier.fillMaxSize()

    )
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun ScanQrCodeScreenPreview() {
    ScanQrCodeScreen(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator()
    )
}