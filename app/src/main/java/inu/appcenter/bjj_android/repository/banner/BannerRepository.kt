package inu.appcenter.bjj_android.repository.banner

import inu.appcenter.bjj_android.model.banner.Banner
import inu.appcenter.bjj_android.repository.base.BaseRepository
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow


interface BannerRepository : BaseRepository {
    suspend fun getBanners() : Flow<CustomResponse<Banner>>


}