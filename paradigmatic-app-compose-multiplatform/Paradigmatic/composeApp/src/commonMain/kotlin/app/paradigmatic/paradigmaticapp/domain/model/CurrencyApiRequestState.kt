package app.paradigmatic.paradigmaticapp.domain.model

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