package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
internal fun Hint(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textAlign: TextAlign? = TextAlign.Center,
) {
    Text(
        text = text,
        color = color,
        style = style,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
internal fun Hint(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textAlign: TextAlign? = TextAlign.Center,
) {
    Text(
        text = text,
        color = color,
        style = style,
        modifier = modifier,
        textAlign = textAlign
    )
}