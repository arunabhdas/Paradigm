package app.paradigmatic.paradigmaticapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.ui.theme.SecondaryColor
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
fun PermissionsInfoDialog(
    title: String? = "Message",
    description: String? = "Your Message",
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    navigator: DestinationsNavigator,
    onDismiss: () -> Unit,
    onBottomButtonTapped: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {

        Box(
            modifier = Modifier
                .height(460.dp)
        ) {
            Column(
                modifier = Modifier
            ) {
                Spacer(modifier = Modifier.height(130.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(25.dp, 10.dp, 25.dp, 10.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = title ?: "Title",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = permissionTextProvider.getDescription(
                                isPermanentlyDeclined = isPermanentlyDeclined
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = onBottomButtonTapped,
                            colors= ButtonDefaults.buttonColors(
                                backgroundColor = SecondaryColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5.dp))
                        ) {
                            Text(
                                text = "Enable Location",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            Image(
                painter = painterResource(id = R.drawable.map_marker_location),
                contentDescription = stringResource(id = R.string.content_description),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(200.dp)
            )

        }
    }
}
