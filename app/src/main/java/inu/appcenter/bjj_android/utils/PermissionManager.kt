package inu.appcenter.bjj_android.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
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
import inu.appcenter.bjj_android.fcm.FcmManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 앱의 권한을 관리하는 전용 클래스
 */
class PermissionManager(
    private val context: Context,
    private val fcmManager: FcmManager? = null
) {
    private val TAG = "PermissionManager"

    // 알림 권한 상태를 추적
    private val _notificationPermissionState = MutableStateFlow<Boolean?>(null)
    val notificationPermissionState: StateFlow<Boolean?> = _notificationPermissionState.asStateFlow()

    /**
     * 알림 권한이 있는지 확인합니다
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            // 상태 저장소 업데이트
            _notificationPermissionState.value = granted

            granted
        } else {
            // Android 13 미만에서는 항상 true 반환
            _notificationPermissionState.value = true
            true
        }
    }

    /**
     * 권한 변경을 처리합니다
     */
    fun onPermissionResult(permission: String, isGranted: Boolean) {
        when (permission) {
            Manifest.permission.POST_NOTIFICATIONS -> {
                Log.d(TAG, "알림 권한 결과: $isGranted")
                _notificationPermissionState.value = isGranted

                // FCM 관리자에게 권한 변경 알림
                fcmManager?.onNotificationPermissionChanged(isGranted)
            }
        }
    }

    /**
     * 초기 권한 상태를 체크합니다
     */
    fun checkInitialPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission()
        }
    }
}

/**
 * Compose에서 알림 권한을 관리하기 위한 Composable
 */
@Composable
fun NotificationPermissionHandler(
    permissionManager: PermissionManager,
    shouldRequestPermission: Boolean = true
) {
    // Android 13 (API Level 33) 이상에서만 실행
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return
    }

    val context = LocalContext.current
    var requestPermission by remember { mutableStateOf(false) }

    // 권한 요청 런처 생성
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // 권한 결과 처리
        permissionManager.onPermissionResult(Manifest.permission.POST_NOTIFICATIONS, isGranted)
    }

    // 권한 확인
    LaunchedEffect(Unit) {
        if (shouldRequestPermission && !permissionManager.hasNotificationPermission()) {
            requestPermission = true
        }
    }

    // 권한 요청
    LaunchedEffect(requestPermission) {
        if (requestPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            requestPermission = false
        }
    }
}