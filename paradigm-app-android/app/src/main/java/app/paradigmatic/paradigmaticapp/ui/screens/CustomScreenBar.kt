package app.paradigmatic.paradigmaticapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.R
import androidx.compose.foundation.layout.Box


@Composable
fun CustomScreenBar(
    title: String,
    onLeftIconClick: () -> Unit,
    onRightIconClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .padding(top = 40.dp)
            .fillMaxWidth(),
    ) {
        // Left IconButton
        IconButton(
            onClick = onLeftIconClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Image(
                painter = painterResource(id = R.drawable.appicon),
                contentDescription = "Menu Icon",
                modifier = Modifier.size(24.dp)
            )
        }
        // Centered Title
        Text(
            text = title,
            color = Color.White,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(Alignment.Center)

        )

        // Right IconButton
        IconButton(
            onClick = {
                onRightIconClick()
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bars_staggered),
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun CustomScreenBarPreview() {
    CustomScreenBar(
        "Beacon Pro Tools",
        {},
        {}
    )
}