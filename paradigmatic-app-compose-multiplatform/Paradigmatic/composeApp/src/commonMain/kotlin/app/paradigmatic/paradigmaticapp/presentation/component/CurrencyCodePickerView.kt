package app.paradigmatic.paradigmaticapp.presentation.component

import androidx.compose.animation.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorMatrix
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyCode
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@Composable
fun CurrencyCodePickerView (
    code: CurrencyCode,
    isSelected: Boolean,
    onSelect: (CurrencyCode) -> Unit
) {
    val saturation = remember { Animatable(if (isSelected) 1f else 0f) }

    LaunchedEffect(isSelected) {
        saturation.animateTo(if (isSelected) 1f else 0f )
    }

    val colorMatrix = remember(saturation.value) {
        ColorMatrix().apply {
            setToSaturation(saturation.value)
        }
    }

}