package app.paradigmatic.paradigmaticapp.ui.screens

import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.ui.theme.PrimaryColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopAppBar(
    headertitle: String,
    title: String,
    onLeftIconClick: () -> Unit,
    onRightIconClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = PrimaryColor,
        contentColor = Color.White,
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left IconButton
            IconButton(
                onClick = onLeftIconClick
            ) {
                Image(
                    painter = painterResource(id = R.drawable.appicon),
                    contentDescription = "Menu Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                headertitle,
                color = Color.White,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(Alignment.CenterVertically
                )
            )
            // Right IconButton
            IconButton(
                onClick = {
                    onRightIconClick()
                },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bars_staggered),
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
