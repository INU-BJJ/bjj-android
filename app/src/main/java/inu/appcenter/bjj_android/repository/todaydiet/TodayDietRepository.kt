package inu.appcenter.bjj_android.repository.todaydiet

import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.model.todaydiet.TodayMenuRes
import retrofit2.Response
import retrofit2.http.Query

interface TodayDietRepository {
    suspend fun getTodayDiet(
        cafeteriaName: String
    ) : Response<List<TodayDietRes>>
}