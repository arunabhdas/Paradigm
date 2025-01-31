package app.paradigmatic.paradigmaticapp.data.beacon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import app.paradigmatic.paradigmaticapp.ui.theme.TertiaryColor
import timber.log.Timber


@Composable
fun EmptyBeaconDeviceItem(
    item: IBeacon,
    initialType: String
) {
    val semiTransparentBlue = PrimaryColor.copy(alpha = 0.5f)
    Card(onClick = {
        Timber.d("Click ${item.getUUID()}")

        // TODO-FIXME- navController?.navigate(DishDetails.route + "/${dish.id}")
    },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(TertiaryColor.copy(alpha = 0.0f)),
        colors = CardDefaults.cardColors(containerColor = semiTransparentBlue)
    ) {
        //TODO: Insert code here
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column() {

                Text(
                    text = "Name: ${item.getName()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                )

                Text(
                    text = "${item.getUUID()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                )

                Text(
                    text = "Type: ${initialType}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                )

                Text(
                    text = "RSSI: ${item.getRssi()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(
                            top = 5.dp,
                            start = 8.dp,
                            bottom = 5.dp,
                            end = 8.dp
                        )
                )

                Text(
                    text = "Major: ${item.getMajor()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(
                            top = 5.dp,
                            start = 8.dp,
                            bottom = 5.dp,
                            end = 8.dp
                        )
                )

                Text(
                    text = "Minor: ${item.getMinor()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(
                            top = 5.dp,
                            start = 8.dp,
                            bottom = 5.dp,
                            end = 8.dp
                        )
                )

                Text(
                    text = "TxPower: ${item.getTxPower()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(
                            top = 5.dp,
                            start = 8.dp,
                            bottom = 5.dp,
                            end = 8.dp
                        )
                )

                Text(
                    text = "Mac: ${item.getAddress()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
            /*
            Image(
                painter = painterResource(
                    id = dish.imageResource),
                contentDescription = "Logo Background",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
            )
            */
        }
    }
    Divider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        thickness = 1.dp,
        color = TertiaryColor
    )
}