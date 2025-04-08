package inu.appcenter.bjj_android.repository.cafeterias

import inu.appcenter.bjj_android.model.cafeteria.CafeteriaInfoResponse
import inu.appcenter.bjj_android.repository.base.BaseRepository
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow

interface CafeteriasRepository : BaseRepository {
    suspend fun getCafeterias() : Flow<CustomResponse<List<String>>>

    suspend fun getCafeteriaInfo(
        name: String
    ) : Flow<CustomResponse<CafeteriaInfoResponse>>
}