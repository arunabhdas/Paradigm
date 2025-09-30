package app.paradigmatic.paradigmaticapp.ui.screens.sidedrawernav
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.ui.navigation.MenuItem
import app.paradigmatic.paradigmaticapp.ui.theme.ThemeUtils
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource


@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "BeaconProTools",
            fontSize = 36.sp
        )
    }

}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
) {
    val gradientBrush = Brush.linearGradient(ThemeUtils.GradientColors)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
    ) {
        // Row for the logo and title at the top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 32.dp, end = 32.dp)
        ) {
            Image(
                painterResource(id = R.drawable.appicon),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        // Column for the rest of the items, centered
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp), // Adjust padding to properly position the items below the logo
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(modifier) {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemClick(item)
                            }
                            .padding(32.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            tint = Color.White,
                            contentDescription = item.contentDescription
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = item.title,
                            style = itemTextStyle,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DrawerBodyPreview() {
    DrawerBody(
        items = listOf(
            MenuItem(
                id = "home",
                title = "Home",
                route = "home_screen",
                contentDescription = "Navigate to Home",
                icon = Icons.Default.Home
            ),
            MenuItem(
                id = "maintenancerequests",
                title = "Maintenance Requests",
                route = "maintenance_requests_screen",
                contentDescription = "Navigate to Maintenace Requests Screen",
                icon = Icons.Default.Settings
            ),
            MenuItem(
                id = "contact",
                title = "Contact",
                route = "contact_screen",
                contentDescription = "Navigate to Contact",
                icon = Icons.Default.Settings
            ),
            MenuItem(
                id = "notifications",
                title = "Notification",
                route = "notification_screen",
                contentDescription = "Navigate to Notifications",
                icon = Icons.Default.Notifications
            )
        ),
        onItemClick = {menuItem ->
        }
    )
}