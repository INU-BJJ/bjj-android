package inu.appcenter.bjj_android.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat

/**
 * 앱의 알림 채널을 관리하기 위한 클래스
 */
class NotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID_LIKED_MENU = "liked_menu_notifications"
        const val CHANNEL_ID_GENERAL = "general_notifications"
    }

    /**
     * 앱이 시작될 때 호출되어 필요한 모든 알림 채널을 생성합니다
     */
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 기본 알림음 설정
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            // 좋아요한 메뉴 알림 채널
            val likedMenuChannel = NotificationChannel(
                CHANNEL_ID_LIKED_MENU,
                "좋아요한 메뉴 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "좋아요한 메뉴에 관한 알림을 제공합니다."
                setSound(defaultSoundUri, audioAttributes)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 250, 250) // 짧게 진동
            }

            // 일반 앱 알림 채널
            val generalChannel = NotificationChannel(
                CHANNEL_ID_GENERAL,
                "일반 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "앱의 일반적인 알림을 제공합니다."
                setSound(defaultSoundUri, audioAttributes)
            }

            // 채널 등록
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannels(listOf(likedMenuChannel, generalChannel))
        }
    }

    /**
     * 알림 채널을 업데이트합니다
     */
    fun updateNotificationChannel(channelId: String, enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            val channel = notificationManager.getNotificationChannel(channelId)

            channel?.let {
                // 알림 채널을 직접 비활성화할 수는 없지만, 중요도를 변경할 수 있음
                if (!enabled) {
                    it.importance = NotificationManager.IMPORTANCE_NONE
                } else {
                    it.importance = NotificationManager.IMPORTANCE_DEFAULT
                }
                notificationManager.createNotificationChannel(it)
            }
        }
    }

    /**
     * 사용자에게 알림 설정 페이지로 이동하라는 메시지를 표시합니다
     */
    fun areNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}