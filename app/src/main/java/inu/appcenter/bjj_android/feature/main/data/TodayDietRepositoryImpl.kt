package inu.appcenter.bjj_android.feature.main.data

import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.core.data.remote.APIService
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

class TodayDietRepositoryImpl(private val apiService: APIService) : TodayDietRepository {
    override suspend fun getTodayDiet(cafeteriaName: String): Flow<CustomResponse<List<TodayDietRes>>> = safeApiCall {
        apiService.getTodayDiet(cafeteriaName = cafeteriaName)
    }
}

