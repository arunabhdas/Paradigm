package app.paradigmatic.paradigmaticapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.screens.composables.Xlr8ProActionItem
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import com.ramcosta.composedestinations.annotation.Destination

import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HomeScreen(
    navController: NavController,
    navigator: DestinationsNavigator

) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp), // Adjust top padding as needed
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}



@Preview(showBackground = true, name = "HomeActionItem Preview")
@Composable
fun HomeActionItemPreview() {
    MaterialTheme {
        Xlr8ProActionItem(
            imageId = R.drawable.appicon, // Replace with a valid drawable resource ID
            text = "Sample Text",
            contentDescription = "Sample Description"
        ) {
            // TODO-FIXME onClick
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
   HomeScreen(
       navController = rememberNavController(),
       navigator = MockDestinationsNavigator()
   )
}