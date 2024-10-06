package inu.appcenter.bjj_android.repository.todaydiet

import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.model.todaydiet.TodayMenuRes
import inu.appcenter.bjj_android.network.APIService
import retrofit2.Response

class TodayDietRepositoryImpl(private val apiService: APIService) : TodayDietRepository {
    override suspend fun getTodayDiet(cafeteriaName: String): Response<List<TodayDietRes>> {
        return apiService.getTodayDiet(cafeteriaName = cafeteriaName)
    }
}