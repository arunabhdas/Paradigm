package app.paradigmatic.paradigmaticapp.presentation.meme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.domain.model.Meme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import com.skydoves.landscapist.ImageOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import com.skydoves.landscapist.coil3.CoilImage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.Text

@Composable
fun MemeView(
   meme: Meme,
   onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(size = 12.dp))
        .clickable {
            onClick()
        }
    ) {
        Box(modifier = Modifier.size(120.dp)) {
            CoilImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(size = 12.dp))
                    .size(120.dp),
                imageModel = { meme.image },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
            if (meme.isFavorite) {
                Row(
                    modifier = Modifier
                        .padding(all = 6.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = Color.Yellow
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier.padding(vertical = 6.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = meme.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )

                Text(
                    text = meme.description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            }
        }
    }
}