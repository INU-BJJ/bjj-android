package inu.appcenter.bjj_android.repository.banner

import inu.appcenter.bjj_android.model.banner.Banner
import inu.appcenter.bjj_android.network.APIService
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow

class BannerRepositoryImpl(private val apiService: APIService) : BannerRepository {
    override suspend fun getBanners(): Flow<CustomResponse<Banner>> = safeApiCall {
        apiService.getBanners()
    }
}