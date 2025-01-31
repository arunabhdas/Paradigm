package app.paradigmatic.paradigmaticapp.data.blescanner

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.ui.theme.TertiaryColor
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun Xlr8ProBeaconItem(
    // TODO-FIXME-DEPRECATE navController: NavHostController? = null,
    navigator: DestinationsNavigator,
    item: Xlr8ProBeacon
) {
    Card(onClick = {
        Log.d("LOGDEBUG", "Click ${item.id}")

        // TODO-FIXME- navController?.navigate(DishDetails.route + "/${dish.id}")
    }) {
        //TODO: Insert code here
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column() {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(
                            top = 5.dp,
                            bottom = 5.dp
                        )
                )
                Text(
                    text = "${item.uuid}",
                    style = MaterialTheme.typography.bodyMedium,
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