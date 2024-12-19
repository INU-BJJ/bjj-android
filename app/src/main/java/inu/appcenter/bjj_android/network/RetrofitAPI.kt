package inu.appcenter.bjj_android.network

import com.google.gson.GsonBuilder
import inu.appcenter.bjj_android.local.DataStoreManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitAPI(private val dataStoreManager: DataStoreManager) : KoinComponent {

    var gson= GsonBuilder().setLenient().create()

    private val BASE_URL = "https://bjj.inuappcenter.kr/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }


    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(dataStoreManager))
        .addInterceptor(loggingInterceptor)
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