package app.paradigmatic.paradigmaticapp.domain.model


sealed class PostApiRequestState<out T> {
    data object Idle: PostApiRequestState<Nothing>()
    data object Loading: PostApiRequestState<Nothing>()
    data class Success<out T>(val data: T) : PostApiRequestState<T>()
    data class Error(val message: String) : PostApiRequestState<Nothing>()

    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun isSuccess(): Boolean = this is Success

    fun getSuccessData() = (this as Success).data
    fun getErrorMessage(): String = (this as Error).message
}

