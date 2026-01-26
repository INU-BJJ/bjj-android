package inu.appcenter.bjj_android.feature.main.data

import inu.appcenter.bjj_android.model.banner.Banner
import inu.appcenter.bjj_android.core.data.remote.APIService
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

class BannerRepositoryImpl(private val apiService: APIService) : BannerRepository {
    override suspend fun getBanners(): Flow<CustomResponse<Banner>> = safeApiCall {
        apiService.getBanners()
    }
}