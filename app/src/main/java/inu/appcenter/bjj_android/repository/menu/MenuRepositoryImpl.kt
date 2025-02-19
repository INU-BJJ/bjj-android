package inu.appcenter.bjj_android.repository.menu

import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.model.menu.MenuRanking
import inu.appcenter.bjj_android.network.APIService
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class MenuRepositoryImpl(private val apiService: APIService) : MenuRepository {
    override suspend fun toggleMenuLiked(mainMenuId: Long): Response<Boolean> {
        return apiService.toggleMenuLiked(mainMenuId = mainMenuId)
    }

    override suspend fun getLikedMenus(): Response<LikedMenu> {
        return apiService.getLikedMenus()
    }

    override suspend fun getMenusRanking(pageNumber: Int, pageSize: Int): Flow<CustomResponse<MenuRanking>> = flow {
        emit(CustomResponse.Loading())
        val response = apiService.getMenusRanking(pageNumber, pageSize)
        emit(CustomResponse.Success(response))
    }.catch { e ->
        emit(CustomResponse.Error(e))
    }
}