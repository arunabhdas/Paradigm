package app.paradigmatic.paradigmaticapp.ui.screens.composables

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskStop
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
fun RoutePlannerToolBottomSheetContent(
    navigator: DestinationsNavigator,
    stops: List<TaskStop>,
    description: String,
    onItemOneClick: () -> Unit,
    onHideButtonClick: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            ListItem(
                headlineContent = { Text(text = "${description}") },
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
        items(stops) { stop ->
            ListItem(
                headlineContent = { Text(text = "${stop.formattedAddressOrigin} - ${stop.formattedAddressDestination}") },
                leadingContent = { Icon(imageVector = Icons.Default.Place, contentDescription = null) },
                modifier = Modifier.clickable {
                    // Consider passing 'stop' to onItemOneClick if you want specific behavior per stop
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
