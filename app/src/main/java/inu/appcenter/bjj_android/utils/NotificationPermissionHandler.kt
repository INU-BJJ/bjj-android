package inu.appcenter.bjj_android.utils

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import inu.appcenter.bjj_android.local.DataStoreManager
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

@Composable
fun NotificationPermissionHandler() {
    // Only request permission on Android 13+ (API Level 33+)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return
    }

    val context = LocalContext.current
    val dataStoreManager = koinInject<DataStoreManager>()
    var shouldRequestPermission by remember { mutableStateOf(false) }

    // Create permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // We don't need to do anything here, the system will handle the permission status
    }

    // Check if we should show the permission dialog
    LaunchedEffect(Unit) {
        // Only request permission if notifications are enabled in the app settings
        val notificationsEnabled = dataStoreManager.getLikedMenuNotification.first()

        if (notificationsEnabled) {
            shouldRequestPermission = true
        }
    }

    // Request permission if needed
    LaunchedEffect(shouldRequestPermission) {
        if (shouldRequestPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            shouldRequestPermission = false
        }
    }
}