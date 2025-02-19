package app.paradigmatic.paradigmaticapp.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontFamily
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import paradigmatic.composeapp.generated.resources.Res
import paradigmatic.composeapp.generated.resources.bebas_nue_regular


fun createHorizontalGradientEffect(
    colors: List<Color>,
    isVertical: Boolean = true
): Brush {

    val endOffset = if (isVertical) {
        Offset(Float.POSITIVE_INFINITY, 0f)
    } else {
        Offset(0f, Float.POSITIVE_INFINITY)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = endOffset,
        tileMode = TileMode.Clamp
    )
}

fun createGradientEffect(
    colors: List<Color>,
    isVertical: Boolean = true
): Brush {

    // Set the end offset for a vertical gradient (top to bottom)
    val endOffset = if (isVertical) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = endOffset,
        tileMode = TileMode.Clamp
    )
}

fun calculateExchangeRate(source: Double, target: Double): Double {
    return target / source
}

fun convertAmountsUsingExchangeRate(amount: Double, exchangeRate: Double): Double {
    return amount * exchangeRate
}



fun displayCurrentDateTime(): String {
    val currentTimestamp = Clock.System.now()
    val date = currentTimestamp.toLocalDateTime(TimeZone.currentSystemDefault())

    // Format the LocalDate into the desired representation
    val dayOfMonth = date.dayOfMonth
    val month =
        date.month.toString().lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    val year = date.year

    // Determine the suffix for the day of the month
    val suffix = when {
        dayOfMonth in 11..13 -> "th" // Special case for 11th, 12th, and 13th
        dayOfMonth % 10 == 1 -> "st"
        dayOfMonth % 10 == 2 -> "nd"
        dayOfMonth % 10 == 3 -> "rd"
        else -> "th"
    }

    // Format the date in the desired representation
    return "$dayOfMonth$suffix $month, $year."
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GetBebasFontFamily() = FontFamily(Font(Res.font.bebas_nue_regular))