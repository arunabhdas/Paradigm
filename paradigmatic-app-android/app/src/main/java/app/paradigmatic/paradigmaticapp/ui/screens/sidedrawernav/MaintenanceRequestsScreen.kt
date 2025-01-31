package app.paradigmatic.paradigmaticapp.ui.screens.sidedrawernav

import app.paradigmatic.paradigmaticapp.data.model.WorkOrder
import app.paradigmatic.paradigmaticapp.ui.navigation.MockDestinationsNavigator
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import app.paradigmatic.paradigmaticapp.ui.theme.createGradientEffect
import app.paradigmatic.paradigmaticapp.viewmodel.WorkOrderUiState
import app.paradigmatic.paradigmaticapp.viewmodel.WorkOrderViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MaintenanceRequestsScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    viewModel: WorkOrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(60.dp)
        ) {
            Text(
                text = "Work Orders",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    top = 4.dp,
                    bottom = 4.dp
                )
            )

            when (uiState) {
                is WorkOrderUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is WorkOrderUiState.Success -> {
                    WorkOrderTable((uiState as WorkOrderUiState.Success).workOrders)
                }
                is WorkOrderUiState.Error -> {
                    Text(
                        text = (uiState as WorkOrderUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

    }
}


/* TODO-FIXME-CLEANUP
@Destination
@Composable
fun MaintenanceRequestsScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    viewModel: WorkOrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Work Orders",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (uiState) {
            is WorkOrderUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is WorkOrderUiState.Success -> {
                WorkOrderTable((uiState as WorkOrderUiState.Success).workOrders)
            }
            is WorkOrderUiState.Error -> {
                Text(
                    text = (uiState as WorkOrderUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
*/
@Composable
fun StatusChip(status: String) {
    val (backgroundColor, textColor) = when (status.lowercase()) {
        "open" -> Color(0xFFE5F6FD) to Color(0xFF0287D0)
        "in progress" -> Color(0xFFFFF7E6) to Color(0xFFD97706)
        "completed" -> Color(0xFFECFDF5) to Color(0xFF059669)
        else -> Color(0xFFFEF2F2) to Color(0xFFDC2626)
    }

    Surface(
        modifier = Modifier.padding(4.dp),
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = textColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun WorkOrderTable(workOrders: List<WorkOrder>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Title", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(2f))
                Text("Status", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                Text("Priority", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                Text("Due Date", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            }

            Divider()

            // Table Content
            LazyColumn {
                items(workOrders) { workOrder ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            workOrder.title,
                            modifier = Modifier.weight(2f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            StatusChip(workOrder.status)
                        }
                        Text(
                            workOrder.createdAt,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            workOrder.createdAt,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Divider()
                }
            }
        }
    }
}

@Preview
@Composable
fun MaintenanceRequestsScreenPreview() {
    MaintenanceRequestsScreen(
        navController = rememberNavController(),
        navigator = MockDestinationsNavigator(),
        viewModel = hiltViewModel()
    )
}