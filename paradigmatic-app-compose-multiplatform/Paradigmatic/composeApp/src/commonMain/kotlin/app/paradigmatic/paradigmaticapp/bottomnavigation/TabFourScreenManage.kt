package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.ManageViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

class TabFourScreenManage(
    private val number: Int
) : Screen, KoinComponent {
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<ManageViewModel>(parameters = { parametersOf(number) })

        var imageField by viewModel.imageField
        var titleField by viewModel.titleField
        var descriptionField by viewModel.descriptionField
        var categoryField by viewModel.categoryField
        var tagsField by viewModel.tagsField
        var creatorField by viewModel.creatorField

        val snackBarHostState = remember { SnackbarHostState() }
        val localCoroutineScope = rememberCoroutineScope()

        Scaffold(
            snackbarHost = {
                SnackbarHost(snackBarHostState)
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (number == -1) "Create" else "Update"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back arrow icon"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (number == -1) {
                                viewModel.insertMeme(
                                    onSuccess = {
                                        localCoroutineScope.launch {
                                            snackBarHostState.showSnackbar("Insert Success")
                                        }
                                        navigator?.pop()
                                    },
                                    onError = {
                                        localCoroutineScope.launch {
                                            snackBarHostState.showSnackbar("Insert Error")
                                        }
                                    }
                                )
                            } else {
                                viewModel.updateMeme(
                                    onSuccess = {
                                        localCoroutineScope.launch {
                                            snackBarHostState.showSnackbar("Update Success")
                                        }
                                        navigator?.pop()
                                    },
                                    onError = { error ->
                                        localCoroutineScope.launch {
                                            snackBarHostState.showSnackbar(error)
                                        }
                                    }
                                )
                            }
                        }) {
                            Icon(
                                imageVector = if (number == -1) Icons.Default.Add
                                else Icons.Default.Check,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    )
                    .padding(all = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = imageField,
                    onValueChange = { imageField = it },
                    placeholder = { Text(text = "Image") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = titleField,
                    onValueChange = { titleField = it },
                    placeholder = { Text(text = "Summary") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = descriptionField,
                    onValueChange = { descriptionField = it },
                    placeholder = { Text(text = "Description") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = categoryField,
                    onValueChange = { categoryField = it },
                    placeholder = { Text(text = "Category") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = tagsField,
                    onValueChange = { tagsField = it },
                    placeholder = { Text(text = "Tags") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = creatorField,
                    onValueChange = { creatorField = it },
                    placeholder = { Text(text = "Builder") }
                )
            }
        }
    }
}
