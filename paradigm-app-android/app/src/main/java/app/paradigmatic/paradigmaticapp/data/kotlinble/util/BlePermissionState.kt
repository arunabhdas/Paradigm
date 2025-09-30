package app.paradigmatic.paradigmaticapp.data.kotlinble.util

enum class BlePermissionNotAvailableReason {
    PERMISSION_REQUIRED,
    NOT_AVAILABLE,
    DISABLED,
}

sealed class BlePermissionState {
    object Available : BlePermissionState()
    data class NotAvailable(
        val reason: BlePermissionNotAvailableReason,
    ) : BlePermissionState()
}
