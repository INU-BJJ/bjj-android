package inu.appcenter.bjj_android.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat
import inu.appcenter.bjj_android.local.DataStoreManager
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

/**
 * 권한 확인 유틸리티 함수
 */
fun hasNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // 이전 안드로이드 버전에서는 권한이 필요없음
    }
}

@Composable
fun NotificationPermissionHandler() {
    // Android 13 (API Level 33) 이상에서만 실행
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return
    }

    val context = LocalContext.current
    val dataStoreManager = koinInject<DataStoreManager>()
    var shouldRequestPermission by remember { mutableStateOf(false) }

    // 권한 요청 런처 생성
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // 권한 요청 결과 처리
    }

    // 알림 설정이 활성화된 경우에만 권한 확인 및 요청
    LaunchedEffect(Unit) {
        val notificationsEnabled = dataStoreManager.getLikedMenuNotification.first()

        if (notificationsEnabled) {
            // 권한이 없는 경우에만 요청
            if (!hasNotificationPermission(context)) {
                shouldRequestPermission = true
            }
        }
    }

    // 권한 요청
    LaunchedEffect(shouldRequestPermission) {
        if (shouldRequestPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            shouldRequestPermission = false
        }
    }
}