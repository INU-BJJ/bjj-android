package inu.appcenter.bjj_android.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import inu.appcenter.bjj_android.MainActivity
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.model.fcm.FcmTokenRequest
import inu.appcenter.bjj_android.utils.hasNotificationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FcmService : FirebaseMessagingService() {

    private val dataStoreManager: DataStoreManager by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        CoroutineScope(Dispatchers.IO).launch {
            val notificationEnabled = dataStoreManager.getLikedMenuNotification.first()

            // 알림 설정 확인 및 권한 확인
            if (notificationEnabled && hasNotificationPermission(this@FcmService)) {
                // 알림이 활성화되어 있고 권한이 있는 경우 처리
                remoteMessage.notification?.let { notification ->
                    sendNotification(notification.title, notification.body)
                }
            } else {
                Log.d("FcmService", "알림 설정 비활성화 또는 권한 없음")
            }
        }
    }

    private fun sendRegistrationToServer(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("fcmToken", token)
            } catch (e: Exception) {

            }
        }
    }

    private fun sendNotification(title: String?, messageBody: String?){
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.menu_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.filled_heart)
            .setContentTitle(title ?: "메뉴 알림")
            .setContentText(messageBody ?: "좋아요한 메뉴에 새로운 소식이 있어요!")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "좋아요한 메뉴 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "좋아요한 메뉴에 관한 알림을 제공합니다."
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

}