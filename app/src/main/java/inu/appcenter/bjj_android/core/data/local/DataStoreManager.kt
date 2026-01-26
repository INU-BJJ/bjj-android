package inu.appcenter.bjj_android.core.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore("user_prefs")
        val TOKEN_KEY = stringPreferencesKey("token_key")
        val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token_key")
        val LIKED_MENU_NOTIFICATION_KEY = booleanPreferencesKey("liked_menu_notification_key")
    }

    val token: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
        }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }


    // FCM 토큰 저장
    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[FCM_TOKEN_KEY] = token
        }
    }

    // FCM 토큰 조회
    val fcmToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[FCM_TOKEN_KEY]
        }

    // 좋아요 메뉴 알림 설정 저장
    suspend fun saveLikedMenuNotification(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LIKED_MENU_NOTIFICATION_KEY] = enabled
        }
    }

    // 좋아요 메뉴 알림 설정 조회
    val getLikedMenuNotification: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[LIKED_MENU_NOTIFICATION_KEY] ?: true // 기본값은 true (활성화)
        }
}