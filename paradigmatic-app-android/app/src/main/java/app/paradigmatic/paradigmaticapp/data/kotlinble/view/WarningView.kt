package app.paradigmatic.paradigmaticapp.data.kotlinble.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.ui.theme.Xlr8ProTheme

@Composable
fun WarningView(
    imageVector: ImageVector,
    title: String,
    hint: String,
    modifier: Modifier = Modifier,
    hintTextAlign: TextAlign? = TextAlign.Center,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        BigIcon(imageVector = imageVector)

        Title(text = title)

        Hint(text = hint, textAlign = hintTextAlign)

        content()
    }
}

@Composable
fun WarningView(
    imageVector: ImageVector,
    title: String,
    hint: AnnotatedString,
    modifier: Modifier = Modifier,
    hintTextAlign: TextAlign? = TextAlign.Center,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BigIcon(imageVector = imageVector)

        Title(text = title)

        Hint(text = hint, textAlign = hintTextAlign)

        content()
    }
}

@Composable
fun WarningView(
    painterResource: Painter,
    title: String,
    hint: String,
    modifier: Modifier = Modifier,
    hintTextAlign: TextAlign? = TextAlign.Center,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BigIcon(painterResource = painterResource)

        Title(text = title)

        Hint(text = hint, textAlign = hintTextAlign)

        content()
    }
}

@Preview
@Composable
private fun WarningViewPreview() {
    Xlr8ProTheme {
        WarningView(
            imageVector = Icons.Filled.Warning,
            title = "Warning",
            hint = "This is a warning view",
            modifier = Modifier.fillMaxSize(),
        )
    }
}