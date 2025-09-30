package app.paradigmatic.paradigmaticapp.ui.routetaskmanager.components

import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.ui.theme.Orange
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun RouteProfileHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .padding(start = 16.dp)
        )

        BadgedBox(
            badge = {
                Badge(
                    backgroundColor = Orange,
                    contentColor = Color.White,
                    modifier = Modifier.offset(y = 1.dp, x = (-3).dp)

                )
            },
            modifier = Modifier.padding(end = 16.dp)
        ){
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_bell),
                contentDescription = "Notifications",
                tint = Color.White
            )
        }
    }
}