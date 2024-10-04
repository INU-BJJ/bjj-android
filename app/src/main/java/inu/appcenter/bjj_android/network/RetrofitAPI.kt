package inu.appcenter.bjj_android.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitAPI : KoinComponent {

    private const val BASE_URL = "https://bjj.inuappcenter.kr/"

    //    private val tokenManager: TokenManager by inject()

//    private val okHttpClient: OkHttpClient by lazy {
////        val context: Context = get()
//
//        OkHttpClient.Builder()
//            .addInterceptor(ErrorResponseInterceptor())
//            .addInterceptor(AuthInterceptor(dataStoreManager))
////            .addInterceptor(TokenInterceptor(tokenManager))
////            .addInterceptor(TokenExpirationInterceptor(context = context))
//            .build()
//    }

//    private val gson: Gson by lazy {
//        GsonProvider.getGson()
//    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
            .build()
    }

    val apiService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}