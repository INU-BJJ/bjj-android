package inu.appcenter.bjj_android.repository.todaydiet

import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.repository.base.BaseRepository
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow

interface TodayDietRepository : BaseRepository {
    suspend fun getTodayDiet(
        cafeteriaName: String
    ) : Flow<CustomResponse<List<TodayDietRes>>>
}