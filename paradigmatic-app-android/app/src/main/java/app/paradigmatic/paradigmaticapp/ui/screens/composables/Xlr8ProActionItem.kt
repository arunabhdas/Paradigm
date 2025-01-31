package app.paradigmatic.paradigmaticapp.ui.screens.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Xlr8ProActionItem(
    imageId: Int,
    text: String,
    contentDescription: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(60.dp)
                .clickable(onClick = onClick)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable(onClick = onClick)
        )
    }
}