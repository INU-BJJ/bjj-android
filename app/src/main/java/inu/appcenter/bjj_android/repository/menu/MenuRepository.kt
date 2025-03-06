package inu.appcenter.bjj_android.repository.menu

import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.model.menu.MenuRanking
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MenuRepository {
    suspend fun toggleMenuLiked(mainMenuId: Long) : Response<Boolean>

    suspend fun getLikedMenus() : Response<List<LikedMenu>>

    suspend fun getMenusRanking(
        pageNumber: Int,
        pageSize: Int
    ) : Flow<CustomResponse<MenuRanking>>
}