package inu.appcenter.bjj_android.repository.menu

import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.model.menu.MenuRanking
import inu.appcenter.bjj_android.network.APIService
import inu.appcenter.bjj_android.utils.CustomResponse
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