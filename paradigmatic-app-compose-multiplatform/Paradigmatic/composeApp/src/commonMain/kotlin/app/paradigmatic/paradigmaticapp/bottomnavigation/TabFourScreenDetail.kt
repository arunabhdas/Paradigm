package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.presentation.viewmodel.DetailViewModel
import com.skydoves.landscapist.coil3.CoilImage
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.skydoves.landscapist.ImageOptions
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite

class TabFourScreenDetail(
    val number: Int,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<DetailViewModel>(parameters = { parametersOf(number) })
        val selectedMeme by viewModel.selectedMeme
        val isFavorite by viewModel.isFavorite
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Details")},
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit icon"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.setFavoriteMeme()
                                navigator?.push(TabFourScreenManage(
                                    number = -1
                                ))
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .alpha(if (isFavorite) 1f else 0.38f),
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Star icon"
                            )
                        }
                        IconButton(
                            onClick = {
                                // TODO-FIXME-BRINGBACK viewModel.deleteMeme()
                                viewModel.setFavoriteMeme()
                                navigator?.push(TabFourScreenManage(
                                    number = number
                                ))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete icon"
                            )
                        }
                    }

                )
            },
        )
        { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 12.dp)
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                ) {
                    CoilImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(size = 12.dp))
                            .fillMaxWidth(),
                        imageModel = { selectedMeme?.image },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = selectedMeme?.category ?: "",
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = selectedMeme?.title ?: "",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = selectedMeme?.description ?: ""
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = selectedMeme?.creator ?: "",
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = selectedMeme?.tags ?: "",
                )
                /* TODO-FIXME
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    selectedMeme?.tags?.forEach { tag ->
                        Text(
                            modifier = Modifier.alpha(0.5f),
                            text = selectedMeme?.tags ?: "",
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
                */
            }
        }
    }
}

