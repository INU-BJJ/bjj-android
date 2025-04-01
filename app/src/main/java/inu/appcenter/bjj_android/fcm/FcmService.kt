package inu.appcenter.bjj_android.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import inu.appcenter.bjj_android.MainActivity
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.notification.NotificationManager.Companion.CHANNEL_ID_GENERAL
import inu.appcenter.bjj_android.notification.NotificationManager.Companion.CHANNEL_ID_LIKED_MENU
import inu.appcenter.bjj_android.utils.PermissionManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FcmService : FirebaseMessagingService() {
    private val TAG = "FcmService"
    private val dataStoreManager: DataStoreManager by inject()
    private val fcmManager: FcmManager by inject()
    private val permissionManager: PermissionManager by inject()

    // 서비스용 코루틴 스코프
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "코루틴 오류: ${throwable.message}", throwable)
    })

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "새 FCM 토큰: $token")

        // FCM 관리자에게 새 토큰 전달
        fcmManager.onTokenRefresh(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "FCM 메시지 수신됨")

        // 알림 권한 및 설정 확인을 위한 코루틴 실행
        serviceScope.launch {
            try {
                // 알림 설정 상태 확인
                val notificationEnabled = dataStoreManager.getLikedMenuNotification.first()

                // 권한 및 알림 설정 확인
                if (notificationEnabled && permissionManager.hasNotificationPermission()) {
                    // 메시지 데이터 처리
                    val messageType = remoteMessage.data["type"] ?: "general"

                    // 알림 표시
                    remoteMessage.notification?.let { notification ->
                        val title = notification.title ?: getString(R.string.app_name)
                        val body = notification.body ?: "새로운 알림이 있습니다"

                        // 메시지 타입에 따라 다른 채널 사용
                        val channelId = if (messageType == "liked_menu") {
                            CHANNEL_ID_LIKED_MENU
                        } else {
                            CHANNEL_ID_GENERAL
                        }

                        sendNotification(title, body, channelId)
                    }
                } else {
                    Log.d(TAG, "알림 설정 비활성화 또는 권한 없음")
                }
            } catch (e: Exception) {
                Log.e(TAG, "알림 처리 중 오류 발생: ${e.message}", e)
            }
        }
    }

    private fun sendNotification(title: String, messageBody: String, channelId: String) {
        try {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                // 필요한 경우 추가 데이터 포함
                putExtra("notification_source", "fcm")
            }

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.filled_heart)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())

            Log.d(TAG, "알림이 성공적으로 표시됨")
        } catch (e: Exception) {
            Log.e(TAG, "알림 표시 중 오류 발생: ${e.message}", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 서비스 종료 시 코루틴 취소
        serviceScope.cancel()
    }
}