package inu.appcenter.bjj_android.di

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KoinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@KoinApp)
            modules(viewModelModule)
        }
    }

}