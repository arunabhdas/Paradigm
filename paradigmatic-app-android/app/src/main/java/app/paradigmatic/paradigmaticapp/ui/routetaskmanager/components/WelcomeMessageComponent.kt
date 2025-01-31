package app.paradigmatic.paradigmaticapp.ui.routetaskmanager.components

import app.paradigmatic.paradigmaticapp.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeMessageComponent(
    title: String,
    subtitle: String,
    number: Number

) {
    val message = if (title.isEmpty()) subtitle else "Hello $title"
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = message,
            fontFamily = FontFamily(Font(R.font.nunito_extrabold)),
            fontSize = 22.sp,
            color = Color.White
        )

        Text(
            text = "$number stops for today",
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 22.sp,
            color = Color.LightGray
        )
    }
}

@Preview
@Composable
fun WelcomeMessageComponentPreview(
) {
    WelcomeMessageComponent(
        title = "Jason",
        subtitle = "No routes available",
        number = 4
    )
}