package app.paradigmatic.paradigmaticapp.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.R

@Composable
fun CustomAppBar(
    onNavigationIconClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(60.dp)
            .padding(top = 40.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onNavigationIconClick
        ) {
            Image(
                painter = painterResource(id = R.drawable.bars_staggered),
                contentDescription = "Menu Icon",
                modifier = Modifier.size(24.dp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.top_center),
            contentDescription = "PropzPro",
            modifier = Modifier.fillMaxWidth(0.5F)
                .padding(
                    horizontal = 20.dp,
                    vertical = 0.dp
                )
        )
        IconButton(onClick = { }) {
            Image(
                painter = painterResource(id = R.drawable.bars_staggered),
                contentDescription = "Cart",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun CustomAppBarPreview() {
   CustomAppBar(
       {}
   )
}