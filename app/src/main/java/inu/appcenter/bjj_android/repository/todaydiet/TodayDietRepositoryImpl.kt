package inu.appcenter.bjj_android.repository.todaydiet

import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.network.APIService
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow

class TodayDietRepositoryImpl(private val apiService: APIService) : TodayDietRepository {
    override suspend fun getTodayDiet(cafeteriaName: String): Flow<CustomResponse<List<TodayDietRes>>> = safeApiCall {
        apiService.getTodayDiet(cafeteriaName = cafeteriaName)
    }
}

