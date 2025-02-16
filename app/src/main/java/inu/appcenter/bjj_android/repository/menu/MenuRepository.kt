package inu.appcenter.bjj_android.repository.menu

import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.model.menu.MenuRanking
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MenuRepository {
    suspend fun toggleMenuLiked(mainMenuId: Long) : Response<Boolean>

    suspend fun getLikedMenus() : Response<LikedMenu>

    suspend fun getMenusRanking(
        pageNumber: Int,
        pageSize: Int
    ) : Flow<CustomResponse<MenuRanking>>
}