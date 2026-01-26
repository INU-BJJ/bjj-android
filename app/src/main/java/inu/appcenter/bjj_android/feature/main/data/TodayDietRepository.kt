package inu.appcenter.bjj_android.feature.main.data

import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.core.data.BaseRepository
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

interface TodayDietRepository : BaseRepository {
    suspend fun getTodayDiet(
        cafeteriaName: String
    ) : Flow<CustomResponse<List<TodayDietRes>>>
}