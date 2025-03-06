package inu.appcenter.bjj_android.repository.menu


import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.model.menu.MenuRanking
import inu.appcenter.bjj_android.repository.base.BaseRepository
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow

interface MenuRepository : BaseRepository {
    suspend fun toggleMenuLiked(mainMenuId: Long): Flow<CustomResponse<Boolean>>
    suspend fun getLikedMenus(): Flow<CustomResponse<LikedMenu>>
    suspend fun getMenusRanking(pageNumber: Int, pageSize: Int): Flow<CustomResponse<MenuRanking>>
}