package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import cafe.adriel.voyager.core.screen.Screen

class TabOneScreen: Screen {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Markets Screen Text")
        }
    }
}


class TabTwoScreen : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Trending",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Trending Screen")
        }
    }
}

class TabThreeScreen : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Home Screen")
        }
    }
}

class TabFourScreen : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Bookmarks",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Bookmarks Screen")
        }
    }
}

class TabFiveScreen : Screen {
    @Composable
    override fun Content() {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Profile") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Navigate"
                        )
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Notifications") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Navigate"
                        )
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Privacy") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Privacy"
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Navigate"
                        )
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Help & Support") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Help"
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Navigate"
                        )
                    }
                )
            }
        }
    }
}

