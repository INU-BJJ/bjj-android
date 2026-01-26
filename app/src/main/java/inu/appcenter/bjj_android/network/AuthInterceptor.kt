package inu.appcenter.bjj_android.network

import inu.appcenter.bjj_android.local.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.atomic.AtomicReference

class AuthInterceptor(private val dataStoreManager: DataStoreManager) : Interceptor {

    // 토큰을 메모리에 캐싱 (Thread-safe)
    private val cachedToken = AtomicReference<String?>(null)

    // Interceptor 전용 CoroutineScope
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        // DataStore의 토큰 변경사항을 실시간으로 감지하여 캐시 업데이트
        scope.launch {
            dataStoreManager.token.collect { newToken ->
                cachedToken.set(newToken)
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // 캐시된 토큰 사용 (디스크 I/O 없음, 빠름)
        val token = cachedToken.get()

        val request = chain.request().newBuilder()
            .apply {
                token?.let {
                    addHeader("Authorization", "Bearer $it")
                }
            }
            .build()

        return chain.proceed(request)
    }
}