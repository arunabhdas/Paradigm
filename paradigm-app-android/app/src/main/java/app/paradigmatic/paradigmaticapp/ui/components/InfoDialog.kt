package app.paradigmatic.paradigmaticapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun InfoDialog(
    title: String? = "Message",
    description: String? = "Your Message",
    navigator: DestinationsNavigator,
    onDismiss: () -> Unit,
    onAffirmative: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .height(500.dp)
        ) {
            Column(
                modifier = Modifier
            ) {
                // Add spacing for the icon overlay
                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = PrimaryColor,
                            shape = RoundedCornerShape(25.dp, 10.dp, 25.dp, 10.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Add top spacing to account for the icon
                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            text = title ?: "Title",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = description ?: "Description",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onAffirmative,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp, end = 32.dp)
                        ) {
                            Text(
                                text = "Enable Permissions",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // App Icon overlay
            Image(
                painter = painterResource(id = R.drawable.appicon),
                contentDescription = stringResource(id = R.string.content_description),
                modifier = Modifier
                    .size(80.dp)
                    .offset(y = (0).dp)
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Info Dialog Preview"
)
@Composable
fun InfoDialogPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            InfoDialog(
                title = stringResource(R.string.location_permission_title),
                description = stringResource(R.string.location_permission_desc),
                navigator = MockDestinationsNavigator(),
                onDismiss = { /* Preview only */ },
                onAffirmative = { /* Preview only */ }
            )
        }
    }
}
