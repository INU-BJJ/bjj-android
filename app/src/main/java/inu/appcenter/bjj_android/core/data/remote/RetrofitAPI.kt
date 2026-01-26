package inu.appcenter.bjj_android.core.data.remote

import com.google.gson.GsonBuilder
import inu.appcenter.bjj_android.BuildConfig
import inu.appcenter.bjj_android.core.data.local.DataStoreManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitAPI(private val dataStoreManager: DataStoreManager) : KoinComponent {

    var gson= GsonBuilder().setLenient().create()

    private val BASE_URL = "https://bjj.inuappcenter.kr/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(dataStoreManager))
        .apply {
            // 디버그 빌드에서만 HTTP 로깅 활성화
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(loggingInterceptor)
            }
        }
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}