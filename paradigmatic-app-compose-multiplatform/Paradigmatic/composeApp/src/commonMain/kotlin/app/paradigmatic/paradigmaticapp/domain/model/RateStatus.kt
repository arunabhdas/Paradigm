package app.paradigmatic.paradigmaticapp.domain.model

import androidx.compose.ui.graphics.Color
import app.paradigmatic.paradigmaticapp.ui.theme.freshColor
import app.paradigmatic.paradigmaticapp.ui.theme.staleColor


enum class RateStatus (
    val title: String,
    val color: Color
) {
    Idle(
        title = "Rates",
        color = Color.White
    ),
    Fresh(
        title = "Fresh rates",
        color = freshColor
    ),
    Stale(
        title = "Rates are not refreshed",
        color = staleColor
    )
}