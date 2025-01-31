package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import android.content.pm.PackageManager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppVersionInfo() {
    val context = LocalContext.current
    val packageInfo = try {
        context.packageManager.getPackageInfo(context.packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }

    val versionName = packageInfo?.versionName ?: "N/A"
    val versionCode = if (packageInfo != null) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toString()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toString()
        }
    } else {
        "N/A"
    }

    // Now display these values in your UI
    Text(
        text = "Version: $versionName.$versionCode",
        color = Color.White
    )
}