package inu.appcenter.bjj_android.feature.main.data

import inu.appcenter.bjj_android.model.banner.Banner
import inu.appcenter.bjj_android.core.data.BaseRepository
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow


interface BannerRepository : BaseRepository {
    suspend fun getBanners() : Flow<CustomResponse<Banner>>


}