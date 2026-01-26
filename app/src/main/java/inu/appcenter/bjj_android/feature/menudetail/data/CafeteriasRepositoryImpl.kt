package inu.appcenter.bjj_android.feature.menudetail.data

import inu.appcenter.bjj_android.model.cafeteria.CafeteriaInfoResponse
import inu.appcenter.bjj_android.core.data.remote.APIService
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

class CafeteriasRepositoryImpl(private val apiService: APIService) : CafeteriasRepository {
    override suspend fun getCafeterias(): Flow<CustomResponse<List<String>>> = safeApiCall {
        apiService.getCafeterias()
    }

    override suspend fun getCafeteriaInfo(name: String): Flow<CustomResponse<CafeteriaInfoResponse>> = safeApiCall {
        apiService.getCafeteriaInfo(name)
    }
}