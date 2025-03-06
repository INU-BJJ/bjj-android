package inu.appcenter.bjj_android.repository.cafeterias

import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.network.APIService
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class CafeteriasRepositoryImpl(private val apiService: APIService) : CafeteriasRepository {
    override suspend fun getCafeterias(): Flow<CustomResponse<List<String>>> = safeApiCall {
        apiService.getCafeterias()
    }
}