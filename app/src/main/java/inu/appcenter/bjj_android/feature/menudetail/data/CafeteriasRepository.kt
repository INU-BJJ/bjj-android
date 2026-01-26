package inu.appcenter.bjj_android.feature.menudetail.data

import inu.appcenter.bjj_android.model.cafeteria.CafeteriaInfoResponse
import inu.appcenter.bjj_android.core.data.BaseRepository
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

interface CafeteriasRepository : BaseRepository {
    suspend fun getCafeterias() : Flow<CustomResponse<List<String>>>

    suspend fun getCafeteriaInfo(
        name: String
    ) : Flow<CustomResponse<CafeteriaInfoResponse>>
}