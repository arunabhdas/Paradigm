package app.paradigmatic.paradigmaticapp.ui.screens.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun TopRightMenuButtonBottomSheetContent(
    navigator: DestinationsNavigator,
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
