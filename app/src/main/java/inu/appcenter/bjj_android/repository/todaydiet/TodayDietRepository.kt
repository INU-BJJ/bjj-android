package inu.appcenter.bjj_android.repository.todaydiet

import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import retrofit2.Response

interface TodayDietRepository {
    suspend fun getTodayDiet(
        cafeteriaName: String
    ) : Response<List<TodayDietRes>>
}