package app.paradigmatic.paradigmaticapp.domain.model

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

sealed class CurrencyApiRequestState<out T> {
    data object Idle: CurrencyApiRequestState<Nothing>()
    data object Loading: CurrencyApiRequestState<Nothing>()
    data class Success<out T>(val data: T) : CurrencyApiRequestState<T>()
    data class Error(val message: String) : CurrencyApiRequestState<Nothing>()

    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun isSuccess(): Boolean = this is Success

    fun getSuccessData() = (this as Success).data
    fun getErrorMessage(): String = (this as Error).message
}

@Composable
fun <T> CurrencyApiRequestState<T>.DisplayResult(
    onIdle: (@Composable () -> Unit)? = null,
    onLoading: (@Composable () -> Unit)? = null,
    onError: (@Composable (String) -> Unit)? = null,
    onSuccess: @Composable (T) -> Unit,
    transitionSpec: ContentTransform = scaleIn(tween(durationMillis = 400))
        + fadeIn(tween(durationMillis = 800))
        togetherWith scaleOut(tween(durationMillis = 400))
        + fadeOut(
            tween(durationMillis = 800)
        )
    ) {
        AnimatedContent(
            targetState = this,
            transitionSpec = { transitionSpec },
            label = "Content Animation"
        ) { state ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                when(state) {
                    is CurrencyApiRequestState.Idle -> {
                        onIdle?.invoke()
                    }

                    is CurrencyApiRequestState.Loading -> {
                        onLoading?.invoke()
                    }

                    is CurrencyApiRequestState.Error -> {
                        onError?.invoke(state.getErrorMessage())
                    }

                    is CurrencyApiRequestState.Success -> {
                        onSuccess(state.getSuccessData())
                    }
                }
            }
        }
    }