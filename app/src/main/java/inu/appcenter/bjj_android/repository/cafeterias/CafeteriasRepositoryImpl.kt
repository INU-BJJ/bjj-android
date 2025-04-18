package inu.appcenter.bjj_android.repository.cafeterias

import inu.appcenter.bjj_android.model.cafeteria.CafeteriaInfoResponse
import inu.appcenter.bjj_android.network.APIService
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow

class CafeteriasRepositoryImpl(private val apiService: APIService) : CafeteriasRepository {
    override suspend fun getCafeterias(): Flow<CustomResponse<List<String>>> = safeApiCall {
        apiService.getCafeterias()
    }

    override suspend fun getCafeteriaInfo(name: String): Flow<CustomResponse<CafeteriaInfoResponse>> = safeApiCall {
        apiService.getCafeteriaInfo(name)
    }
}