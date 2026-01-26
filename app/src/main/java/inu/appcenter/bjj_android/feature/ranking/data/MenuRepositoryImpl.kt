package inu.appcenter.bjj_android.feature.ranking.data

import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.model.menu.MenuRanking
import inu.appcenter.bjj_android.core.data.remote.APIService
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

class MenuRepositoryImpl(private val apiService: APIService) : MenuRepository {
    override suspend fun toggleMenuLiked(mainMenuId: Long): Flow<CustomResponse<Boolean>> = safeApiCall {
        apiService.toggleMenuLiked(mainMenuId = mainMenuId)
    }

    override suspend fun getLikedMenus(): Flow<CustomResponse<List<LikedMenu>>> = safeApiCall {
        apiService.getLikedMenus()
    }

    override suspend fun getMenusRanking(pageNumber: Int, pageSize: Int): Flow<CustomResponse<MenuRanking>> = safeApiCall {
        apiService.getMenusRanking(pageNumber, pageSize)
    }
}