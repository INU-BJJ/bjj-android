package inu.appcenter.bjj_android.di

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class KoinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // 로깅 레벨 설정 (개발 시에만 활성화하고 프로덕션에서는 Level.NONE으로 설정하세요)
            androidLogger(Level.ERROR)
            androidContext(this@KoinApp)
            modules(allModules)
        }
        setupCoil()
    }

    private fun setupCoil() {
        val imageLoader = ImageLoader.Builder(this)
            .memoryCache { MemoryCache.Builder(this).maxSizePercent(0.25).build() }
            .diskCache { DiskCache.Builder().directory(cacheDir.resolve("image_cache")).maxSizeBytes(5L * 1024 * 1024).build() }
            .build()
        Coil.setImageLoader(imageLoader)
    }
}