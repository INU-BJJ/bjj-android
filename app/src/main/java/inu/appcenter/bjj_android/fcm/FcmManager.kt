package inu.appcenter.bjj_android.fcm

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.model.fcm.FcmTokenRequest
import inu.appcenter.bjj_android.network.APIService
import inu.appcenter.bjj_android.utils.hasNotificationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.min

/**
 * FCM 토큰을 관리하기 위한 전용 클래스
 * 토큰 검색, 저장 및 서버 등록을 처리합니다.
 */
class FcmManager(
    private val context: Context,
    private val dataStoreManager: DataStoreManager,
    private val apiService: APIService
) {
    private val TAG = "FcmManager"
    private val scope = CoroutineScope(Dispatchers.IO)

    // 앱이 시작될 때 호출되어 FCM 토큰을 가져오고 저장합니다
    fun initialize() {
        scope.launch {
            try {
                retrieveAndSaveToken()
            } catch (e: Exception) {
                Log.e(TAG, "초기화 중 오류 발생: ${e.message}", e)
            }
        }
    }

    // 토큰을 가져오고 저장합니다
    private suspend fun retrieveAndSaveToken() {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d(TAG, "FCM 토큰 검색 완료: $token")
            dataStoreManager.saveFcmToken(token)

            // 사용자가 로그인되어 있는지 확인
            val hasToken = dataStoreManager.token.first() != null
            if (hasToken) {
                registerTokenWithServer()
            }
        } catch (e: Exception) {
            Log.e(TAG, "FCM 토큰 검색 중 오류 발생: ${e.message}", e)
            // 지수 백오프로 다시 시도
            retryWithBackoff { retrieveAndSaveToken() }
        }
    }

    // 사용자가 로그인할 때 호출됩니다
    fun onUserLogin() {
        scope.launch {
            registerTokenWithServer()
        }
    }

    // 알림 권한이 변경될 때 호출됩니다
    fun onNotificationPermissionChanged(granted: Boolean) {
        Log.d(TAG, "알림 권한 변경됨: $granted")
        // 권한이 부여된 경우 토큰 등록이 알맞게 처리되었는지 확인
        if (granted) {
            scope.launch {
                val hasToken = dataStoreManager.token.first() != null
                if (hasToken) {
                    registerTokenWithServer()
                }
            }
        }
    }

    // 토큰을 서버에 등록합니다
    suspend fun registerTokenWithServer() {
        try {
            val fcmToken = dataStoreManager.fcmToken.first()
            val userToken = dataStoreManager.token.first()
            val notificationsEnabled = dataStoreManager.getLikedMenuNotification.first()

            if (fcmToken != null && userToken != null) {
                // 알림이 활성화되어 있고 권한이 있는 경우에만 토큰 등록
                if (!notificationsEnabled || !hasNotificationPermission(context)) {
                    Log.d(TAG, "알림이 비활성화되었거나 권한이 없어 토큰을 등록하지 않습니다")
                    return
                }

                Log.d(TAG, "서버에 FCM 토큰 등록 시도: $fcmToken")
                apiService.registerFcmToken(FcmTokenRequest(fcmToken))
                Log.d(TAG, "서버에 FCM 토큰 등록 성공")
            } else {
                Log.d(TAG, "FCM 토큰 또는 사용자 토큰이 null입니다. 등록을 건너뜁니다.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "FCM 토큰 등록 중 오류 발생: ${e.message}", e)
            // 지수 백오프로 다시 시도
            retryWithBackoff { registerTokenWithServer() }
        }
    }

    // FCM 토큰이 갱신될 때 호출됩니다
    fun onTokenRefresh(newToken: String) {
        scope.launch {
            try {
                Log.d(TAG, "새 FCM 토큰 저장: $newToken")
                dataStoreManager.saveFcmToken(newToken)

                val hasToken = dataStoreManager.token.first() != null
                if (hasToken) {
                    registerTokenWithServer()
                }
            } catch (e: Exception) {
                Log.e(TAG, "토큰 새로고침 처리 중 오류 발생: ${e.message}", e)
            }
        }
    }

    // 지수 백오프로 작업을 재시도합니다
    private suspend fun retryWithBackoff(maxRetries: Int = 5, block: suspend () -> Unit) {
        var retryCount = 0
        var retryDelay = 1000L // 초기 지연 1초

        while (retryCount < maxRetries) {
            try {
                delay(retryDelay)
                block()
                return // 성공하면 종료
            } catch (e: Exception) {
                retryCount++
                // 다음 재시도의 지연 계산 (최대 64초)
                retryDelay = min(retryDelay * 2, 64000L)
                Log.d(TAG, "재시도 $retryCount/$maxRetries, 다음 시도까지 ${retryDelay/1000}초 대기")
            }
        }

        Log.e(TAG, "최대 재시도 횟수($maxRetries)에 도달했습니다.")
    }
}