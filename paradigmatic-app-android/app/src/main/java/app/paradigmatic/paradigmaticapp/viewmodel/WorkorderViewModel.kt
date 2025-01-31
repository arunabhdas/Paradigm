package app.paradigmatic.paradigmaticapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import app.paradigmatic.paradigmaticapp.data.model.WorkOrder
import app.paradigmatic.paradigmaticapp.data.supabase.SupabaseClient

sealed class WorkOrderUiState {
    object Loading : WorkOrderUiState()
    data class Success(val workOrders: List<WorkOrder>) : WorkOrderUiState()
    data class Error(val message: String) : WorkOrderUiState()
}

@HiltViewModel
class WorkOrderViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<WorkOrderUiState>(WorkOrderUiState.Loading)
    val uiState: StateFlow<WorkOrderUiState> = _uiState

    init {
        fetchWorkOrders()
    }

    private fun fetchWorkOrders() {
        viewModelScope.launch {
            try {
                val workOrders = SupabaseClient.client.postgrest["workorders"]
                    .select()
                    .decodeList<WorkOrder>()
                _uiState.value = WorkOrderUiState.Success(workOrders)
            } catch (e: Exception) {
                _uiState.value = WorkOrderUiState.Error(e.message ?: "Failed to fetch work orders")
            }
        }
    }
}